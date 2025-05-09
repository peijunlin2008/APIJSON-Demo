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

package apijson.demo;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import apijson.RequestMethod;
import apijson.StringUtil;
import apijson.boot.DemoController;
import apijson.demo.model.Privacy;
import apijson.fastjson2.APIJSONObjectParser;
import apijson.fastjson2.APIJSONParser;
import apijson.orm.SQLConfig;


/**请求解析器
 * 具体见 https://github.com/Tencent/APIJSON/issues/38
 * @author Lemon
 */
public class DemoParser extends APIJSONParser<Long> {

    public static final Map<String, HttpSession> KEY_MAP;
    static {
      KEY_MAP = new HashMap<>();
    }

    public DemoParser() {
        super();
    }
    public DemoParser(RequestMethod method) {
        super(method);
    }
    public DemoParser(RequestMethod method, boolean needVerify) {
        super(method, needVerify);
    }


    @Override
    public DemoParser setNeedVerify(boolean needVerify) {
        super.setNeedVerify(needVerify);
        return this;
    }

    //    //	可重写来设置分页页码是否从 1 开始，true - 从 1 开始；false - 从 0 开始
//    @Override
//    public boolean isStartFrom1() {
//        return true;
//    }

    private int maxQueryCount = 2000;
    //	可重写来设置最大查询数量
    @Override
    public int getMaxQueryCount() {
      return maxQueryCount;
    }

    @Override
    public int getMaxUpdateCount() {
        return 2000;
    }

    @Override
    public int getMaxObjectCount() {
        return getMaxUpdateCount();
    }

    @Override
    public int getMaxSQLCount() {
        return getMaxUpdateCount();
    }

    @Override
    public JSONObject parseResponse(JSONObject request) {
        try {  // 内部使用，可通过这种方式来突破限制，获得更大的自由度
          HttpSession session = KEY_MAP.get(request.getString("key"));
          //DemoVerifier.verifyLogin(session);
          if (session != null) {
            request.remove("key");
            maxQueryCount = 1000;
          }
        } catch (Exception e) {}

        return super.parseResponse(request);
    }


    @Override
    public APIJSONObjectParser<Long> createObjectParser(JSONObject request, String parentPath
            , SQLConfig<Long, JSONObject, JSONArray> arrayConfig
            , boolean isSubquery, boolean isTable, boolean isArrayMainTable) throws Exception {
        return new DemoObjectParser(getSession(), request, parentPath, arrayConfig
                , isSubquery, isTable, isArrayMainTable).setMethod(getMethod()).setParser(this);
    }

    // 实现应用层与数据库共用账号密码，可用于多租户、SQLAuto 等 <<<<<<<<<<<<<<<<
    private boolean asDBAccount;
    private String dbAccount;
    private String dbPassword;
    @Override
    public APIJSONParser<Long> setSession(HttpSession session) {
        Boolean asDBAccount = session == null ? null : (Boolean) session.getAttribute(DemoController.AS_DB_ACCOUNT);
        this.asDBAccount = asDBAccount != null && asDBAccount;
        if (this.asDBAccount) {
          // User user = (User) session.getAttribute(DemoController.USER_);
          // this.dbAccount = user.getName();
          Privacy privacy = session == null ? null : (Privacy) session.getAttribute(DemoController.PRIVACY_);
          this.dbAccount = privacy.getPhone();
          this.dbPassword = privacy.get__password();
        }

        return super.setSession(session);
    }

    @Override
    public JSONObject executeSQL(SQLConfig<Long, JSONObject, JSONArray> config, boolean isSubquery) throws Exception {
        if (asDBAccount && config instanceof DemoSQLConfig) {
          DemoSQLConfig cfg = (DemoSQLConfig) config;
          if (StringUtil.isEmpty(cfg.gainDBAccount())) {
            cfg.setDBAccount(dbAccount);
          }
          if (StringUtil.isEmpty(cfg.gainDBPassword())) {
            cfg.setDBPassword(dbPassword);
          }
        }
        return super.executeSQL(config, isSubquery);
    }

    // 实现应用层与数据库共用账号密码，可用于多租户、SQLAuto 等 >>>>>>>>>>>>>>>

}
