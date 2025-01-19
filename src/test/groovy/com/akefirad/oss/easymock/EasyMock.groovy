package com.akefirad.oss.easymock

class EasyMock {
    public static <T> T mock(Class<T> classToMock) {
        return org.easymock.EasyMock.mock(classToMock)
    }
}
