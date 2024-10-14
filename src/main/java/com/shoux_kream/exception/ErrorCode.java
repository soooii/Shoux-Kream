package com.shoux_kream.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCode {

    INVALID_ID(BAD_REQUEST, BAD_REQUEST.value(), "id does not exist in database. please check again"),
    BAD_ARGUMENT(BAD_REQUEST, BAD_REQUEST.value(), "bad argument, please check our api"),
    BAD_BUSINESS_LOGIC(BAD_REQUEST, BAD_REQUEST.value(), "invalid logic for this service"),
    NO_AUTHENTICATION(UNAUTHORIZED, -101, "no authentication. please log in"),
    NO_AUTHORITY(FORBIDDEN, -102, "you have no authorization for this access"),
    INVALID_LOGIN_INFO(BAD_REQUEST, -103, "invalid email or password."),
    INVALID_SESSION_FORMAT(BAD_REQUEST, -104, "invalid session format"),
    SESSION_EXPIRATION(BAD_REQUEST, -105, "session has expired."),
    INVALID_AUCTION_BIDDING(BAD_REQUEST, BAD_REQUEST.value(),"this is not the time to bid for an auction"),
    INVALID_REQUEST_VALUE(BAD_REQUEST, BAD_REQUEST.value(),"please check request value again"),
    AFTER_DUE_DATE(BAD_REQUEST, BAD_REQUEST.value(), "biding is expired."),
    OVER_PRICE(BAD_REQUEST, BAD_REQUEST.value(), "too much bidding price"),
    INSUFFICIENT_ACCOUNT_MONEY(BAD_REQUEST, -201, "not enough account money"),
    SELL_BIDDING_NOT_AUTHENTICATED(BAD_REQUEST, BAD_REQUEST.value(), "bidding is not authenticated."),
    CANNOT_CANCEL(BAD_REQUEST, BAD_REQUEST.value(), "bidding cannot be cancelled."),
    SERVER_ERROR(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.value(), "Something does not worked. please call xxx)xxx-xxxx"),
    INVALID_BIDDING_PRICE(BAD_REQUEST,BAD_REQUEST.value(),"please enter a higher price than the highest bid price"),
    INVALID_CHANGE_STATUS(BAD_REQUEST,BAD_REQUEST.value(),"you cannot change ongoing or finished auction to before state"),
    INVALID_CANCEL_BIDDING(BAD_REQUEST,BAD_REQUEST.value(),"please check auction id and price"),
    BANKING_SERVICE_ERROR(BAD_REQUEST, -202, "banking services are currently unavailable. please try again"),
    BIDDING_NOT_LIVE(BAD_REQUEST, BAD_REQUEST.value(), "Bidding is not live. please transact other bidding."),
    BIDDING_NOT_IN_TRANSACTION(BAD_REQUEST, BAD_REQUEST.value(), "Bidding is not in transaction. please check yout bidding id again."),
    BIDDING_NOT_DEPOSIT(BAD_REQUEST, BAD_REQUEST.value(), "Bidding is not deposited yet. please send money first."),
    BIDDING_ABUSING(BAD_REQUEST, BAD_REQUEST.value(), "cannot transact bidding for same owner"),

    ;

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String description;

    ErrorCode(HttpStatus httpStatus, Integer code, String description) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.description = description;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
