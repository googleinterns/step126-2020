package com.google.sps.servlet

import javax.servlet.http._

class TestScalaServlet extends HttpServlet {
    override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
        resp.setContentType("text/html")
        resp.getWriter.println(json)
    }

    private val json = """{ "name": "World" }"""
}

