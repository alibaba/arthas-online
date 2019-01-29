package com.alibaba.arthas.online;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SocketUtils;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;

@Service
public class DockerService {
	private static final Logger logger = LoggerFactory.getLogger(DockerService.class);

	@Autowired
	private AppProperties appProperties;

	@Autowired(required = false)
	private DockerClient dockerClient;

	private Path dockerFilePath;

	private String arthasOnlineImage;

	@PostConstruct
	public void init() throws URISyntaxException, DockerException, InterruptedException, IOException {

		String dockerFileDirectory = appProperties.getDockerFileDirectory();
		if (dockerFileDirectory == null || dockerFileDirectory.isEmpty()) {
			URL resource = DockerService.class.getClassLoader().getResource("docker/Dockerfile");
			dockerFilePath = Paths.get(resource.toURI()).getParent();
		} else {
			dockerFilePath = Paths.get(dockerFileDirectory);
		}

		logger.info("dockerFilePath: {}", dockerFilePath);

		if(dockerClient != null) {
			arthasOnlineImage = dockerClient.build(dockerFilePath);
		}
	}

	public String startContainer() throws DockerException, InterruptedException {

		int tcpPort = SocketUtils.findAvailableTcpPort(10000);

		// Bind container ports to host ports
		final String[] ports = { "8563" };
		final Map<String, List<PortBinding>> portBindings = new HashMap<>();
		List<PortBinding> hostPorts = new ArrayList<>();
		hostPorts.add(PortBinding.of("0.0.0.0", tcpPort));
		portBindings.put("8563", hostPorts);

		final HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).build();

		// Create container with exposed ports
		final ContainerConfig containerConfig = ContainerConfig.builder().hostConfig(hostConfig)
				.image(arthasOnlineImage).tty(true).attachStdin(true).attachStderr(true).attachStdout(true)
				.exposedPorts(ports).cmd("/bin/sh", "-c", "/bin/sh /tmp/attach.sh").build();

		final ContainerCreation creation = dockerClient.createContainer(containerConfig);
		final String id = creation.id();

		// Inspect container
		final ContainerInfo info = dockerClient.inspectContainer(id);

		// Start container
		dockerClient.startContainer(id);

		return id;
	}

	public void stopContainer(String containerId) throws DockerException, InterruptedException {
		dockerClient.stopContainer(containerId, 3);
	}

	public ContainerInfo queryConatiner(String containerId) throws DockerException, InterruptedException {
		// TODO use docker.listContainers?
		return dockerClient.inspectContainer(containerId);
	}

	// 这个函数要有cache
	public int runningContainerSize() {
		// docker.listContainers(params);
		return 0;
	}

}
