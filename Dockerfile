FROM gradle:6.7-jdk11

USER gradle
COPY --chown=gradle:gradle . /usr/src/gocd-generic-oauth-authorization-plugin
WORKDIR /usr/src/gocd-generic-oauth-authorization-plugin
RUN gradle --no-daemon clean test assemble && \
		echo Build successful: && \
	ls -l build/libs/*.jar

