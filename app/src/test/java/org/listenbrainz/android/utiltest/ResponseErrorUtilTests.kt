package org.listenbrainz.android.utiltest

import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import org.listenbrainz.android.model.ApiError
import org.listenbrainz.android.model.GeneralError
import org.listenbrainz.android.model.ResponseError.Companion.getSocialResponseError
import org.listenbrainz.android.model.ResponseError.Companion.parseError
import org.listenbrainz.android.model.SocialData
import org.listenbrainz.android.model.SocialError
import org.listenbrainz.android.model.SocialResponse
import org.listenbrainz.sharedtest.testdata.SocialRepositoryTestData.ErrorUtil.alreadyFollowingError
import org.listenbrainz.sharedtest.testdata.SocialRepositoryTestData.ErrorUtil.authHeaderNotFoundError
import org.listenbrainz.sharedtest.testdata.SocialRepositoryTestData.ErrorUtil.cannotFollowSelfError
import org.listenbrainz.sharedtest.testdata.SocialRepositoryTestData.ErrorUtil.userNotFoundError
import org.listenbrainz.sharedtest.utils.ResourceString.cannot_follow_self_error
import org.listenbrainz.sharedtest.utils.ResourceString.user_does_not_exist_error
import retrofit2.Response

class ResponseErrorUtilTests {
    
    @Test
    fun parseErrorTest() {
        var error = parseError(Response.error<SocialData>(404, user_does_not_exist_error.toResponseBody()))
        assertEquals(ApiError(404, userNotFoundError), error)
        
        error = parseError(Response.error<SocialResponse>(400, cannot_follow_self_error.toResponseBody()))
        assertEquals(ApiError(400, cannotFollowSelfError), error)
    }
    
    @Test
    fun getSocialResponseErrorTest() {
        var result = getSocialResponseError(
            Response.error<SocialData>(
                404,
                userNotFoundError.toResponseBody())
        )
        assertEquals(SocialError.USER_NOT_FOUND, result)
        
        result = getSocialResponseError(
            Response.error<SocialData>(
                401,
                authHeaderNotFoundError.toResponseBody())
        )
        assertEquals(GeneralError.AUTH_HEADER_NOT_FOUND, result)
        
        result = getSocialResponseError(
            Response.error<SocialData>(
                400,
                alreadyFollowingError.toResponseBody())
        )
        assertEquals(SocialError.ALREADY_FOLLOWING, result)
        
        result = getSocialResponseError(
            Response.error<SocialData>(
                400,
                cannotFollowSelfError.toResponseBody())
        )
        assertEquals(SocialError.CANNOT_FOLLOW_SELF, result)
        
        result = getSocialResponseError(
            Response.error<SocialData>(
                429,
                "".toResponseBody())
        )
        assertEquals(GeneralError.RATE_LIMIT_EXCEEDED, result)
        
        result = getSocialResponseError(
            Response.error<SocialData>(
                400,
                "Wow new error".toResponseBody())
        )
        assertEquals(GeneralError.UNKNOWN, result)
    }
}