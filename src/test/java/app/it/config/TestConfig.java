package app.it.config;

@EnableTransactionManagement
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ITConfig.class, Application.class, WebMvcConfigMock.class})
@ActiveProfiles("integration-test")
@Ignore
public class TestConfig {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Value("${local.server.port}")
    protected int port;

    @Value("${integration.test.host}")
    protected String host;

    @Value("${server.context-path}")
    protected String contextPath;
}
