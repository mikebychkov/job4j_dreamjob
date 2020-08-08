package com.dream.servlet;

import com.dream.store.Store;
import com.dream.store.PsqlStore;
import com.dream.store.StoreStub;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class RegServletTest {

    @Test
    public void test() throws ServletException, IOException {
        Store store = new StoreStub();
        PowerMockito.mockStatic(PsqlStore.class);
        when(PsqlStore.instOf()).thenReturn(store);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getParameter("name")).thenReturn("Mike");
        when(req.getParameter("email")).thenReturn("mike@mail");
        when(req.getParameter("password")).thenReturn("m1ke");

        new RegServlet().doPost(req, resp);

        assertEquals(store.findUser("mike@mail").getName(), "Mike");
    }
}
