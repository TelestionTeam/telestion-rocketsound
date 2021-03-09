package de.jvpichowski.rocketsound.messages.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.telestion.api.message.JsonMessage;

public record Velocity(@JsonProperty double measured, @JsonProperty double estimated) implements JsonMessage {

	@SuppressWarnings("unused")
	public Velocity() {
		this(0.0f, 0.0f);
	}
}
