package com.hn.exception;
/**
 * 自定义异常（运行期异常）：重复秒杀异常
 * @author Linlihua
 *
 */
public class RepeatKillException extends SeckillException{

	public RepeatKillException() {
		super();
	}

	public RepeatKillException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepeatKillException(String message) {
		super(message);
	}

}
