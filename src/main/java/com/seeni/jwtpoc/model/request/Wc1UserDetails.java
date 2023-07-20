package com.seeni.jwtpoc.model.request;

public record Wc1UserDetails(String eblDocumentId, String rid, String language, String userCode,
                             String companyCode, String timeZone, String signature, String action) {
}
