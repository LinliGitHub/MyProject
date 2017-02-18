package com.hn.exception;
/**
 * 秒杀业务相关异常
 * @author Linlihua
 *
 */
public class SeckillException extends RuntimeException {

	public SeckillException() {
		super();
	}

	public SeckillException(String message, Throwable cause) {
		super(message, cause);
	}

	public SeckillException(String message) {
		super(message);
	}

}
