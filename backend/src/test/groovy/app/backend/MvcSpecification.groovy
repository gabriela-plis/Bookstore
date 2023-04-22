package app.backend

import app.backend.security.CustomUserDetailsService
import app.backend.security.RestAuthenticationSuccessHandler
import app.backend.security.SecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@Import(SecurityConfig.class)
abstract class MvcSpecification extends Specification {

    @MockBean
    CustomUserDetailsService userDetailsService

    @MockBean
    RestAuthenticationSuccessHandler successHandler

    @Autowired
    MockMvc mvc
}
