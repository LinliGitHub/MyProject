package com.hn.auth.context;

import com.hn.model.User;

public class UserContext implements AutoCloseable {

	public static final ThreadLocal<User> current = new ThreadLocal<User>();

	public UserContext(User user) {
		current.set(user);
	}

	public static User getCurrentUser() {
		return current.get();
	}

	public void close() throws Exception {
		current.remove();
	}

}
