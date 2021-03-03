package de.jvpichowski.rocketsound.messages.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.telestion.api.message.JsonMessage;

// TODO: float not yet supported by vertx -> changed floats to double (Jan)
public record GpsData(
		@JsonProperty int satCount,
		@JsonProperty boolean fix,
		@JsonProperty float latitude,
		@JsonProperty float longitude,
		@JsonProperty long time) implements JsonMessage {
	@SuppressWarnings("unused")
	public GpsData(){
		this(0, false, 0.0f, 0.0f, 0);
	}
}
