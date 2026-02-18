package io.maksymdobrynin.snowflakegenerator

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class BitwiseUtilsTest {
	@Nested
	inner class Value5Bits {
		private val length5Bits: Long = 5L
		private val value5Bits: Long = 0b11111

		@ParameterizedTest
		@CsvSource(
			value = [
				"0, 3, 7", // 0b111
				"0, 5, 31", // 0b11111
				"2, 1, 1", // 0b1
			],
		)
		fun `Should extract correct subset of bits successfully`(
			startBit: Int,
			bitCount: Int,
			expectedResult: Long,
		) {
			val actualResult = BitwiseUtils.extract(
				src = value5Bits,
				len = length5Bits,
				startBit = startBit,
				bitCount = bitCount,
			)
			assertThat(actualResult).isEqualTo(expectedResult)
		}

		@Test
		fun `should throw exception when startBit is out of range`() {
			assertThatCode {
				BitwiseUtils.extract(
					src = value5Bits,
					len = length5Bits,
					startBit = 6,
					bitCount = 3,
				)
			}
				.isExactlyInstanceOf(IllegalArgumentException::class.java)
				.hasMessage(
					"Starting bit must match range 0..${length5Bits - 1}, " +
						"otherwise it will be out of bounds.",
				)
		}

		@Test
		fun `should throw exception when bitCount is out of range`() {
			assertThatCode {
				BitwiseUtils.extract(
					src = value5Bits,
					len = length5Bits,
					startBit = 0,
					bitCount = 6,
				)
			}
				.isExactlyInstanceOf(IllegalArgumentException::class.java)
				.hasMessage(
					"Count of bits to be extracted " +
						"must match range of a given number that is 1..$length5Bits.",
				)
		}

		@Test
		fun `should throw exception when startBit + bitCount exceeds length`() {
			assertThatCode {
				BitwiseUtils.extract(
					src = value5Bits,
					len = length5Bits,
					startBit = 3,
					bitCount = 3,
				)
			}
				.isExactlyInstanceOf(IllegalArgumentException::class.java)
				.hasMessage(
					"Starting bit in combination with count of bits to be extracted [6] " +
						"must not exceed the length of a given number: $length5Bits.",
				)
		}
	}
}
