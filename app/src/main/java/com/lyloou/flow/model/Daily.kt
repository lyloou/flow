/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lyloou.flow.model

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.26 19:01
 *
 *
 * Description:
 */
class Daily {
    /**
     * sid : 2640
     * tts : http://news.iciba.com/admin/tts/2017-06-26-day
     * content : Time would heal almost all wounds. If your wounds have not been healed up, please wait for a short while.
     * note : 时间几乎会愈合所有伤口，如果你的伤口还没有愈合，请给时间一点时间！
     * love : 2027
     * translation : 词霸小编：给时间一点时间，让过去成为过去。给过去一点时间，让时间成为过去。
     * picture : http://cdn.iciba.com/news/word/20170626.jpg
     * picture2 : http://cdn.iciba.com/news/word/big_20170626b.jpg
     * caption : 词霸每日一句
     * dateline : 2017-06-26
     * s_pv : 0
     * sp_pv : 0
     * tags : [{"id":null,"name":null}]
     * fenxiang_img : http://cdn.iciba.com/web/news/longweibo/imag/2017-06-26.jpg
     */
    var sid: String? = null
    var tts: String? = null
    var content: String? = null
    var note: String? = null
    var love: String? = null
    var translation: String? = null
    var picture: String? = null
    var picture2: String? = null
    var caption: String? = null
    var dateline: String? = null
    var s_pv: String? = null
    var sp_pv: String? = null
    var fenxiang_img: String? = null
    var tags: List<Tags>? = null

    class Tags {
        var id: String? = null
        var name: String? = null
    }
}