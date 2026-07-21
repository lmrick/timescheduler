package com.lmrick.timescheduler.security;

import java.util.Date;

public record JwtTokenInfo(
				String username,
				Long version,
				Date expiration
) {

}
