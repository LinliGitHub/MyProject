package com.hn.exception;
/**
 * 秒杀关闭异常
 * @author Linlihua
 *
 */
public class SeckillCloseException extends SeckillException{

	public SeckillCloseException() {
		super();
	}

	public SeckillCloseException(String message, Throwable cause) {
		super(message, cause);
	}

	public SeckillCloseException(String message) {
		super(message);
	}

}
