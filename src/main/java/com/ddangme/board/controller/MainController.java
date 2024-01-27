package com.ddangme.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {

        /*
        Forward (전달)
        - 서버 내부에서 이동: 서버 내부에서 이루어진다. 클라이언트의 브라우저는 실제로 새로운 요청을 하지 않는다.
        - 단일 요청 및 응답: 하나의 서블릿에서 다른 서블릿으로 제어를 넘긴다. 이때, 클라이언트는 이동을 알지 못하고 브라우저 주소창에 이동한 주소가 표시되지 않는다.
        - 내부 데이터 공유: Forward로 이동한 서블릿은 원래의 요청에서 받은 데이터를 그대로 사용할 수 있습니다.

        Redirect (리다이렉트)
        - 클라이언트에게 새로운 주소로 이동 명령: Redirect는 서버가 클라이언트에게 새로운 주소로 이동할 것을 알려주고, 클라이언트는 새로운 주소로 새로운 요청을 보냅니다.
        - 클라이언트 측 이동: 브라우저는 새로운 주소로 이동하며, 새로운 주소가 브라우저 주소창에 나타납니다.
        - 새로운 요청 및 응답: 새로운 요청이 발생하므로, 서버는 새로운 요청을 처리하고 응답합니다.
        - 이전 요청과 새로운 요청 간에는 데이터 공유가 어려움: Redirect로 이동한 후에는 새로운 요청이므로, 이전에 요청에서 받은 데이터를 그대로 사용하기 어렵습니다.


        요약
        - Forward는 서버 내부에서 이동하며, 클라이언트는 알지 못합니다.
        - Redirect는 클라이언트에게 새로운 주소로 이동할 것을 알리고, 클라이언트는 새로운 주소로 새로운 요청을 보냅니다.
        - Forward는 단일 요청 및 응답이며, 서버 내부 데이터를 그대로 사용할 수 있습니다.
        - Redirect는 새로운 요청과 응답이 발생하며, 이전 요청에서 받은 데이터를 그대로 사용하기 어렵습니다.
         */
        return "forward:/articles";
    }
}