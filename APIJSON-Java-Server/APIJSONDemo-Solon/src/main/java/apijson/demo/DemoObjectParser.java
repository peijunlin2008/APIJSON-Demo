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

import apijson.orm.AbstractObjectParser;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import java.util.List;


import apijson.NotNull;
import apijson.RequestMethod;
import apijson.orm.Join;
import apijson.orm.SQLConfig;
import org.noear.solon.core.handle.SessionState;


/**对象解析器，用来简化 Parser
 * @author Lemon
 */
public class DemoObjectParser extends AbstractObjectParser<Long, JSONObject, JSONArray> {

    public DemoObjectParser(SessionState session, @NotNull JSONObject request, String parentPath
            , SQLConfig<Long, JSONObject, JSONArray> arrayConfig
            , boolean isSubquery, boolean isTable, boolean isArrayMainTable) throws Exception {
        super(request, parentPath, arrayConfig, isSubquery, isTable, isArrayMainTable);
    }


    @Override
    public SQLConfig<Long, JSONObject, JSONArray> newSQLConfig(RequestMethod method, String table, String alias
            , JSONObject request, List<Join<Long, JSONObject, JSONArray>> joinList, boolean isProcedure) throws Exception {
        return DemoSQLConfig.newSQLConfig(method, table, alias, request, joinList, isProcedure);
    }

}
