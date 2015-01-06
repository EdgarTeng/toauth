package com.tenchael.toauth.commons;

import org.apache.commons.codec.digest.DigestUtils;

public class DEncryptionUtils {

	public static String sha256Hex(String rawStr) {
		return DigestUtils.sha256Hex(rawStr);
	}

	public static void main(String[] args) {

		p("==================md5Hex======================");

		for (int i = 0; i < 10; i++) {
			p(DigestUtils.md5Hex("tes2"));
		}

		p("==================sha1Hex======================");

		for (int i = 0; i < 10; i++) {
			p(DigestUtils.sha1Hex("tes2"));
		}

		p("===================sha256Hex=====================");

		for (int i = 0; i < 10; i++) {
			p(DigestUtils.sha256Hex("tes2"));
		}

		p("=====================sha384Hex===================");

		for (int i = 0; i < 10; i++) {
			p(DigestUtils.sha384Hex("tes2"));
		}

		p("====================sha512Hex====================");

		for (int i = 0; i < 10; i++) {
			p(DigestUtils.sha512Hex("tes2"));
		}

	}

	private static void p(Object obj) {
		System.out.println(obj);
	}

}
