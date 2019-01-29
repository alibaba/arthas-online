package com.alibaba.arthas.online;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

	private int containerLimit = 10;

	private boolean initDockerClient = false;

	private String dockerFileDirectory;

	public int getContainerLimit() {
		return containerLimit;
	}

	public void setContainerLimit(int containerLimit) {
		this.containerLimit = containerLimit;
	}

	public boolean isInitDockerClient() {
		return initDockerClient;
	}

	public void setInitDockerClient(boolean initDockerClient) {
		this.initDockerClient = initDockerClient;
	}

	public String getDockerFileDirectory() {
		return dockerFileDirectory;
	}

	public void setDockerFileDirectory(String dockerFileDirectory) {
		this.dockerFileDirectory = dockerFileDirectory;
	}
}
