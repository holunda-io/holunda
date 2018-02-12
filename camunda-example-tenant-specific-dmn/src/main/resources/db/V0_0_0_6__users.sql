-- User (password = username)
INSERT INTO ACT_ID_USER (ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, PICTURE_ID_) VALUES ('james', 1, 'James', 'Hetfield', NULL, '{SHA-512}YrIK9SZQwnz1AvJ8rWm4PkVgltgvHAz13IrOaMNb6zih9XFX55WqRPCSeDjrBM+2fzewWVdeJHc08F42+NF5lQ==', 'Km6blGeLL+Ud+by3IUlozw==', null);
INSERT INTO ACT_ID_USER (ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, PICTURE_ID_) VALUES ('klaus', 1, 'Klaus', 'Kleber', NULL, '{SHA-512}jjIJJshx8RB8opNUUbD9ozYhrfmL1Ho7BGn/rzUkWVOakWR7Qk/F/c3lVenyVPAk//mK++6i0sB+x23rUdXfuA==', 'B73TkMfadVXR1Y8kbF8r/w==', null);
INSERT INTO ACT_ID_USER (ID_, REV_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, PICTURE_ID_) VALUES ('francois', 1, 'Francois', 'Hollande', NULL, '{SHA-512}p3kq5ywsbRgOkcL8kbRiSDju0RXhHwPR89OK8aXgPdRhcGb5toyCiG9sLOnfgs2aBGVyfT9iOhkL0HvcJaVCWA==', '6z8gWnO3GOPBm1M0FBM3vw==', null);

-- for convenience add users to 'camunda-admin' group
INSERT into ACT_ID_MEMBERSHIP (USER_ID_, GROUP_ID_) VALUES ('james', 'camunda-admin');
INSERT into ACT_ID_MEMBERSHIP (USER_ID_, GROUP_ID_) VALUES ('klaus', 'camunda-admin');
INSERT into ACT_ID_MEMBERSHIP (USER_ID_, GROUP_ID_) VALUES ('francois', 'camunda-admin');

-- Tenant memberships
INSERT INTO ACT_ID_TENANT_MEMBER (ID_, TENANT_ID_, USER_ID_, GROUP_ID_) VALUES ('TM01', 'EN', 'james', null);
INSERT INTO ACT_ID_TENANT_MEMBER (ID_, TENANT_ID_, USER_ID_, GROUP_ID_) VALUES ('TM02', 'DE', 'klaus', null);
INSERT INTO ACT_ID_TENANT_MEMBER (ID_, TENANT_ID_, USER_ID_, GROUP_ID_) VALUES ('TM03', 'FR', 'francois', null);

-- Standard authorizations for each individual user (full access to own data)
INSERT INTO ACT_RU_AUTHORIZATION (ID_, REV_, TYPE_, GROUP_ID_, USER_ID_, RESOURCE_TYPE_, RESOURCE_ID_, PERMS_) VALUES ('A011', 1, 1, null, 'james', 1, 'james', 2147483647);
INSERT INTO ACT_RU_AUTHORIZATION (ID_, REV_, TYPE_, GROUP_ID_, USER_ID_, RESOURCE_TYPE_, RESOURCE_ID_, PERMS_) VALUES ('A021', 1, 1, null, 'klaus', 1, 'klaus', 2147483647);
INSERT INTO ACT_RU_AUTHORIZATION (ID_, REV_, TYPE_, GROUP_ID_, USER_ID_, RESOURCE_TYPE_, RESOURCE_ID_, PERMS_) VALUES ('A031', 1, 1, null, 'francois', 1, 'francois', 2147483647);

-- Tenant read authorizations for each individual user
INSERT INTO ACT_RU_AUTHORIZATION (ID_, REV_, TYPE_, GROUP_ID_, USER_ID_, RESOURCE_TYPE_, RESOURCE_ID_, PERMS_) VALUES ('A012', 1, 1, null, 'james', 11, 'EN', 2);
INSERT INTO ACT_RU_AUTHORIZATION (ID_, REV_, TYPE_, GROUP_ID_, USER_ID_, RESOURCE_TYPE_, RESOURCE_ID_, PERMS_) VALUES ('A022', 1, 1, null, 'klaus', 11, 'DE', 2);
INSERT INTO ACT_RU_AUTHORIZATION (ID_, REV_, TYPE_, GROUP_ID_, USER_ID_, RESOURCE_TYPE_, RESOURCE_ID_, PERMS_) VALUES ('A032', 1, 1, null, 'francois', 11, 'FR', 2);
