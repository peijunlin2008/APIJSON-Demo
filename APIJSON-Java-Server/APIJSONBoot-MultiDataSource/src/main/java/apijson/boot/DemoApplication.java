/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package apijson.boot;

import apijson.fastjson2.APIJSONApplication;
import apijson.fastjson2.APIJSONCreator;
import apijson.fastjson2.APIJSONVerifier;
import apijson.fastjson2.APIJSONSQLConfig;
import apijson.orm.AbstractParser;
import apijson.orm.AbstractVerifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;
import java.util.regex.Pattern;

import javax.naming.Context;

import apijson.Log;
import apijson.NotNull;
import apijson.StringUtil;
import apijson.demo.DemoFunctionParser;
import apijson.demo.DemoParser;
import apijson.demo.DemoSQLConfig;
import apijson.demo.DemoSQLExecutor;
import apijson.demo.DemoVerifier;
import apijson.orm.Verifier;
//import unitauto.MethodUtil;
//import unitauto.MethodUtil.Argument;
//import unitauto.MethodUtil.InstanceGetter;
//import unitauto.MethodUtil.JSONCallback;
//import unitauto.jar.UnitAutoApp;


/**
 * Demo SpringBoot Application 主应用程序启动类
 * 右键这个类 > Run As > Java Application
 * 具体见 SpringBoot 文档
 * https://www.springcloud.cc/spring-boot.html#using-boot-locating-the-main-class
 *
 * @author Lemon
 */
@Configuration
@SpringBootApplication
public class DemoApplication implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    private static final String TAG = "DemoApplication";

    // 全局 ApplicationContext 实例，方便 getBean 拿到 Spring/SpringBoot 注入的类实例
    private static ApplicationContext APPLICATION_CONTEXT;
    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    public static void main(String[] args) throws Exception {
        APPLICATION_CONTEXT = SpringApplication.run(DemoApplication.class, args);

        try {
            DemoSQLExecutor.REDIS_TEMPLATE.discard();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        // FIXME 不要开放给项目组后端之外的任何人使用 UnitAuto（强制登录鉴权）！！！如果不需要单元测试则移除相关代码或 unitauto.Log.DEBUG = false;
        // 上线生产环境前改为 false，可不输出 APIJSONORM 的日志 以及 SQLException 的原始(敏感)信息
        //unitauto.Log.DEBUG = true;
        Log.DEBUG = true; // 是否开启调试模式（打印详细日志、返回详细调试信息等）
        AbstractParser.IS_PRINT_BIG_LOG = true; // 是否打印大日志
        APIJSONVerifier.ENABLE_APIJSON_ROUTER = true; // apijson-framework 已集成字段插件 apijson-router，是否开启 接口路由 模式，支持简单接口转为 APIJSON JSON
        //APIJSONParser.IS_START_FROM_1 = true; // 分页页码是否从 1 开始，true - 从 1 开始；false - 从 0 开始
        APIJSONSQLConfig.ENABLE_COLUMN_CONFIG = false; // apijson-framework 已集成字段插件 apijson-column，支持 !key 反选字段 和 字段名映射
        APIJSONApplication.init();
        System.out.println("\n\n<<<<<<<<< 本 Demo 在 resources/static 内置了 APIAuto，Chrome/Firefox 打开 http://localhost:8080 即可调试(端口号根据项目配置而定) ^_^ >>>>>>>>>\n");
    }

    // SpringBoot 2.x 自定义端口方式
    @Override
    public void customize(ConfigurableServletWebServerFactory server) {
        server.setPort(8080); // 9090);
    }

    // 支持 APIAuto 中 JavaScript 代码跨域请求
    @Bean
    public WebMvcConfigurer corsConfig() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("*")
                        .allowCredentials(true)
                        .exposedHeaders(DemoController.APIJSON_DELEGATE_ID)  // Cookie 和 Set-Cookie 怎么设置都没用 ,Cookie,Set-Cookie")   // .exposedHeaders("*")
                        .maxAge(3600);
            }
        };
    }

    static {
        // 把以下需要用到的数据库驱动取消注释即可，如果这里没有可以自己新增
        //		try { //加载驱动程序
        //			Log.d(TAG, "尝试加载 SQLServer 驱动 <<<<<<<<<<<<<<<<<<<<< ");
        //			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        //			Log.d(TAG, "成功加载 SQLServer 驱动！>>>>>>>>>>>>>>>>>>>>> ");
        //		}
        //		catch (ClassNotFoundException e) {
        //			e.printStackTrace();
        //			Log.e(TAG, "加载 SQLServer 驱动失败，请检查 pom.xml 中 net.sourceforge.jtds 版本是否存在以及可用 ！！！");
        //		}
        //
        //		try { //加载驱动程序
        //			Log.d(TAG, "尝试加载 Oracle 驱动 <<<<<<<<<<<<<<<<<<<<< ");
        //			Class.forName("oracle.jdbc.driver.OracleDriver");
        //			Log.d(TAG, "成功加载 Oracle 驱动！>>>>>>>>>>>>>>>>>>>>> ");
        //		}
        //		catch (ClassNotFoundException e) {
        //			e.printStackTrace();
        //			Log.e(TAG, "加载 Oracle 驱动失败，请检查 pom.xml 中 com.oracle.jdbc 版本是否存在以及可用 ！！！");
        //		}
        //
        //		try { //加载驱动程序
        //			Log.d(TAG, "尝试加载 DB2 驱动 <<<<<<<<<<<<<<<<<<<<< ");
        //			Class.forName("com.ibm.db2.jcc.DB2Driver");
        //			Log.d(TAG, "成功加载 DB2 驱动！>>>>>>>>>>>>>>>>>>>>> ");
        //		}
        //		catch (ClassNotFoundException e) {
        //			e.printStackTrace();
        //			Log.e(TAG, "加载 DB2 驱动失败，请检查 pom.xml 中 com.ibm.db2 版本是否存在以及可用 ！！！");
        //		}

        try { //加载驱动程序
            Log.d(TAG, "尝试加载 SQLite 驱动 <<<<<<<<<<<<<<<<<<<<< ");
            Class.forName("org.sqlite.SQLiteJDBCLoader");
            Log.d(TAG, "成功加载 SQLite 驱动！>>>>>>>>>>>>>>>>>>>>> ");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "加载 SQLite 驱动失败，请检查 pom.xml 中 sqlite-jdbc 版本是否存在以及可用 ！！！");
        }

        try { //加载驱动程序
            Log.d(TAG, "尝试加载 Dameng 驱动 <<<<<<<<<<<<<<<<<<<<< ");
            Class.forName("dm.jdbc.driver.DmDriver");
            Log.d(TAG, "成功加载 Dameng 驱动！>>>>>>>>>>>>>>>>>>>>> ");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "加载 Dameng 驱动失败，请检查 pom.xml 中 com.dameng 版本是否存在以及可用 ！！！");
        }

        try { //加载驱动程序
            Log.d(TAG, "尝试加载 Snowflake 驱动 <<<<<<<<<<<<<<<<<<<<< ");
            Class.forName("net.snowflake.client.jdbc.SnowflakeDriver");
            Log.d(TAG, "成功加载 Snowflake 驱动！>>>>>>>>>>>>>>>>>>>>> ");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "加载 Snowflake 驱动失败，请检查 pom.xml 中 net.snowflake 版本是否存在以及可用 ！！！");
        }

        try { //加载驱动程序
            Log.d(TAG, "尝试加载 Databricks 驱动 <<<<<<<<<<<<<<<<<<<<< ");
            Class.forName("com.databricks.client.jdbc.Driver");
            Log.d(TAG, "成功加载 Databricks 驱动！>>>>>>>>>>>>>>>>>>>>> ");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "加载 Databricks 驱动失败，请检查 pom.xml 中 com.databricks 版本是否存在以及可用 ！！！");
        }

        try { //加载驱动程序
            Log.d(TAG, "尝试加载 IoTDB 驱动 <<<<<<<<<<<<<<<<<<<<< ");
            Class.forName("org.apache.iotdb.jdbc.IoTDBDriver");
            Log.d(TAG, "成功加载 IoTDB 驱动！>>>>>>>>>>>>>>>>>>>>> ");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "加载 IoTDB 驱动失败，请检查 pom.xml 中 org.apache.iotdb.jdbc 版本是否存在以及可用 ！！！");
        }

        try { //加载驱动程序
            Log.d(TAG, "尝试加载 DuckDB 驱动 <<<<<<<<<<<<<<<<<<<<< ");
            Class.forName("org.duckdb.DuckDBDriver");
            Log.d(TAG, "成功加载 DuckDB 驱动！>>>>>>>>>>>>>>>>>>>>> ");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "加载 DuckDB 驱动失败，请检查 pom.xml 中 org.duckdb 版本是否存在以及可用 ！！！");
        }

        //    try { //加载驱动程序
        //      Log.d(TAG, "尝试加载 TDengine 驱动 <<<<<<<<<<<<<<<<<<<<< ");
        //      Class.forName("com.taosdata.jdbc.TSDBDriver");
        //      Log.d(TAG, "成功加载 TDengine 驱动！>>>>>>>>>>>>>>>>>>>>> ");
        //    } catch (ClassNotFoundException e) {
        //      e.printStackTrace();
        //      Log.e(TAG, "加载 TDengine 驱动失败，请检查 pom.xml 中 com.taosdata.jdbc 版本是否存在以及可用 ！！！");
        //    }

        // try { //加载驱动程序
        //     Log.d(TAG, "尝试加载 openGauss 驱动 <<<<<<<<<<<<<<<<<<<<< ");
        //     Class.forName("org.opengauss.Driver");
        //     Log.d(TAG, "成功加载 openGauss 驱动！>>>>>>>>>>>>>>>>>>>>> ");
        // } catch (ClassNotFoundException e) {
        //     e.printStackTrace();
        //     Log.e(TAG, "加载 openGauss 驱动失败，请检查 pom.xml 中 org.opengauss 版本是否存在以及可用 ！！！");
        // }

        // APIJSON 配置 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        Map<String, Pattern> COMPILE_MAP = AbstractVerifier.COMPILE_MAP;
        COMPILE_MAP.put("PHONE", StringUtil.PATTERN_PHONE);
        COMPILE_MAP.put("EMAIL", StringUtil.PATTERN_EMAIL);
        COMPILE_MAP.put("ID_CARD", StringUtil.PATTERN_ID_CARD);

        // 使用本项目的自定义处理类
        APIJSONApplication.DEFAULT_APIJSON_CREATOR = new APIJSONCreator<Long>() {

            @Override
            public DemoParser createParser() {
                return new DemoParser();
            }

            @Override
            public DemoFunctionParser createFunctionParser() {
                return new DemoFunctionParser();
            }

            @Override
            public DemoVerifier createVerifier() {
                return new DemoVerifier();
            }

            @Override
            public DemoSQLConfig createSQLConfig() {
                return new DemoSQLConfig();
            }

            @Override
            public DemoSQLExecutor createSQLExecutor() {
                return new DemoSQLExecutor();
            }

        };

        // APIJSON 配置 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


        // UnitAuto 单元测试配置  https://github.com/TommyLemon/UnitAuto  <<<<<<<<<<<<<<<<<<<<<<<<<<<
        // FIXME 不要开放给项目组后端之外的任何人使用 UnitAuto（强制登录鉴权）！！！如果不需要单元测试则移除相关代码或 unitauto.Log.DEBUG = false;
        //UnitAutoApp.init();

        // 适配 Spring 注入的类及 Context 等环境相关的类
        //final InstanceGetter ig = MethodUtil.INSTANCE_GETTER;
        //MethodUtil.INSTANCE_GETTER = new InstanceGetter() {
        //
        //    @Override
        //    public Object getInstance(@NotNull Class<?> clazz, List<Argument> classArgs, Boolean reuse) throws Exception {
        //        if (APPLICATION_CONTEXT != null && ApplicationContext.class.isAssignableFrom(clazz) && clazz.isAssignableFrom(APPLICATION_CONTEXT.getClass())) {
        //            return APPLICATION_CONTEXT;
        //        }
        //
        //        if (reuse != null && reuse && (classArgs == null || classArgs.isEmpty())) {
        //            return APPLICATION_CONTEXT.getBean(clazz);
        //        }
        //
        //        return ig.getInstance(clazz, classArgs, reuse);
        //    }
        //};
        //
        //// 排除转换 JSON 异常的类，一般是 Context 等环境相关的类
        //final JSONCallback jc = MethodUtil.JSON_CALLBACK;
        //MethodUtil.JSON_CALLBACK = new JSONCallback() {
        //
        //    @Override
        //    public JSONObject newSuccessResult() {
        //        return jc.newSuccessResult();
        //    }
        //
        //    @Override
        //    public JSONObject newErrorResult(Throwable e) {
        //        return jc.newErrorResult(e);
        //    }
        //
        //    @Override
        //    public JSONObject parseJSON(String type, Object value) {
        //        if (value == null || unitauto.JSON.isBooleanOrNumberOrString(value) || value instanceof JSON || value instanceof Enum) {
        //            return jc.parseJSON(type, value);
        //        }
        //
        //        if (value instanceof ApplicationContext
        //                || value instanceof Context
        //                || value instanceof org.apache.catalina.Context
        //            // SpringBoot 2.6.7 已移除  || value instanceof ch.qos.logback.core.Context
        //        ) {
        //            value = value.toString();
        //        } else {
        //            try {
        //                value = parseJSON(JSON.toJSONString(value, new PropertyFilter() {
        //                    @Override
        //                    public boolean apply(Object object, String name, Object value) {
        //                        if (value == null) {
        //                            return true;
        //                        }
        //
        //                        if (value instanceof ApplicationContext
        //                                || value instanceof Context
        //                                || value instanceof org.apache.catalina.Context
        //                            // SpringBoot 2.6.7 已移除  || value instanceof ch.qos.logback.core.Context
        //                        ) {
        //                            return false;
        //                        }
        //
        //                        // 防止通过 UnitAuto 远程执行 getDBPassword 等方法来查到敏感信息，但如果直接调用 public String getDBUri 这里没法拦截，仍然会返回敏感信息
        //                        //	if (object instanceof SQLConfig) {
        //                        //		// 这个类部分方法不序列化返回
        //                        //		if ("dBUri".equalsIgnoreCase(name) || "dBPassword".equalsIgnoreCase(name) || "dBAccount".equalsIgnoreCase(name)) {
        //                        //			return false;
        //                        //		}
        //                        //		return false;  // 这个类所有方法都不序列化返回
        //                        //	}
        //
        //                        // 所有类中的方法只要包含关键词就不序列化返回
        //                        String n = StringUtil.toLowerCase(name);
        //                        if (n.contains("database") || n.contains("schema") || n.contains("dburi") || n.contains("password") || n.contains("account")) {
        //                            return false;
        //                        }
        //
        //                        return Modifier.isPublic(value.getClass().getModifiers());
        //                    }
        //                }));
        //            } catch (Exception e) {
        //                Log.e(TAG, "toJSONString  catch \n" + e.getMessage());
        //            }
        //        }
        //
        //        return jc.parseJSON(type, value);
        //    }
        //
        //};

        // UnitAuto 单元测试配置  https://github.com/TommyLemon/UnitAuto  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    }

}
