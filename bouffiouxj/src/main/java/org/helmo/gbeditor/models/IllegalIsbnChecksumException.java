package org.helmo.gbeditor.models;

public class IllegalIsbnChecksumException extends IllegalArgumentException {
	private final int actualChecksum;
	private final int expectedChecksum;

	public IllegalIsbnChecksumException(String message, int expectedChecksum, int actualChecksum) {
		super(message);
		this.actualChecksum = actualChecksum;
		this.expectedChecksum = expectedChecksum;
	}

	public int getActualChecksum() {
		return actualChecksum;
	}

	public int getExpectedChecksum() {
		return expectedChecksum;
	}
}
