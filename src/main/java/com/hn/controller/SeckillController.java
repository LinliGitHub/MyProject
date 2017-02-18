package com.hn.controller;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.hn.dto.Exposer;
import com.hn.dto.SeckillExecution;
import com.hn.dto.SeckillResult;
import com.hn.enums.SeckillStateEnum;
import com.hn.exception.RepeatKillException;
import com.hn.exception.SeckillCloseException;
import com.hn.model.Seckill;
import com.hn.service.SeckillService;

@Controller
@RequestMapping("/seckill") // 模块/资源/{id}/collections
public class SeckillController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SeckillService seckillService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list", list);
		return "list";
	}

	@RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
		if (seckillId == null) {
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if (seckill == null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}

	// ajax接口输出秒杀接口地址
	@RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
		SeckillResult<Exposer> result = null;
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result = new SeckillResult<Exposer>(true, e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") long seckillId,
			@CookieValue(value = "killPhone", required = false) Long userPhone, @PathVariable("md5") String md5) {
		if (userPhone == null) {
			return new SeckillResult<SeckillExecution>(false, "未注册");
		}
		SeckillResult<SeckillExecution> result = null;
		try {
			//改造为存储过程调用秒杀逻辑
//			SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
			SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
			result = new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch (SeckillCloseException e) {
			SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.END);
			return new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch (RepeatKillException e) {
			SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch (Exception e) {
			logger.error(e.getMessage());
			SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.CLOSE);
			return new SeckillResult<SeckillExecution>(true, seckillExecution);
		}
		return result;
	}

	@RequestMapping(value="/time/now",method=RequestMethod.GET,produces={
			"application/json;charset=UTF-8"
	})
	@ResponseBody
	public SeckillResult<Long> time(){
		Date now = new Date();
		return new SeckillResult<Long>(true, now.getTime());
	}
}
