package com.cctv.redis.factory;

import org.redisson.Redisson;
import org.redisson.RedissonClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cctv.redis.conf.RedissonConfig;

@Component
public class RedissonFactoryBean implements DisposableBean,
		FactoryBean<RedissonTemplate> {

	@Autowired
	RedissonConfig config;

	private RedissonClient redisson;

	@Override
	public RedissonTemplate getObject() throws Exception {
		redisson = Redisson.create(config.getConfig());
		RedissonTemplate template = new RedissonTemplate();
		template.setRedisson(redisson);
		return template;
	}

	@Override
	public Class<?> getObjectType() {

		return RedissonTemplate.class;
	}

	@Override
	public boolean isSingleton() {

		return true;
	}

	public RedissonConfig getConfig() {
		return config;
	}

	public void setConfig(RedissonConfig config) {
		this.config = config;
	}

	@Override
	public void destroy() throws Exception {

		if (redisson != null) {
			synchronized (this) {
				if (redisson != null) {
					redisson.shutdown();
				}
			}
		}

	}
}
