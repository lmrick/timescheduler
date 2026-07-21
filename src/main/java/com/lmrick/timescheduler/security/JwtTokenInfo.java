package com.lmrick.timescheduler.security;

import com.lmrick.timescheduler.infrastructure.entity.Role;

import java.util.Date;

public record JwtTokenInfo(
				String username,
				Long version,
				Role role,
				Date expiration
) {

}
