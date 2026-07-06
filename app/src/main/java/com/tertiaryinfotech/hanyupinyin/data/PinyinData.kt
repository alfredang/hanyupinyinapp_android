package com.tertiaryinfotech.hanyupinyin.data

import java.text.Normalizer

/** A single pinyin building block (声母 initial or 韵母 final) with an example. */
data class PinyinUnit(
    val symbol: String,        // e.g. "b", "ang"
    val example: String,       // example syllable, e.g. "bā"
    val exampleHanzi: String,  // example character, e.g. "八"
    val exampleMeaning: String,
)

/** A prompt used in a typing practice session: show [prompt], the user types [answer]. */
data class PinyinPrompt(
    val prompt: String,        // what is shown (hanzi, or a syllable with tone marks)
    val toned: String,         // the toned pinyin (for showing the correct answer)
    val answer: String,        // expected typed pinyin, lowercase, no tone marks, no spaces
    val meaning: String,       // short gloss in 简体中文 / English
    val abbr: String = "",     // optional 简拼 shortcut (initials only), also accepted as correct
)

/** Lowercased, diacritic-folded (tone marks & ü removed), spaces stripped. */
fun String.pinyinPlain(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .lowercase()
        .replace(" ", "")
        .trim()

/** Turn an initial/final into a "show hanzi, type pinyin" prompt. */
fun PinyinUnit.asPrompt(): PinyinPrompt = PinyinPrompt(
    prompt = exampleHanzi,
    toned = example,
    answer = example.pinyinPlain(),
    meaning = exampleMeaning,
)

object PinyinData {

    // 23 声母 (initials)
    val initials: List<PinyinUnit> = listOf(
        PinyinUnit("b", "bā", "八", "八 / eight"),
        PinyinUnit("p", "pá", "爬", "爬 / climb"),
        PinyinUnit("m", "mā", "妈", "妈 / mother"),
        PinyinUnit("f", "fā", "发", "发 / send"),
        PinyinUnit("d", "dà", "大", "大 / big"),
        PinyinUnit("t", "tā", "他", "他 / he"),
        PinyinUnit("n", "nǐ", "你", "你 / you"),
        PinyinUnit("l", "lè", "乐", "乐 / happy"),
        PinyinUnit("g", "gē", "哥", "哥 / brother"),
        PinyinUnit("k", "kàn", "看", "看 / look"),
        PinyinUnit("h", "hǎo", "好", "好 / good"),
        PinyinUnit("j", "jiā", "家", "家 / home"),
        PinyinUnit("q", "qù", "去", "去 / go"),
        PinyinUnit("x", "xué", "学", "学 / study"),
        PinyinUnit("zh", "zhōng", "中", "中 / middle"),
        PinyinUnit("ch", "chī", "吃", "吃 / eat"),
        PinyinUnit("sh", "shū", "书", "书 / book"),
        PinyinUnit("r", "rén", "人", "人 / person"),
        PinyinUnit("z", "zì", "字", "字 / character"),
        PinyinUnit("c", "cài", "菜", "菜 / dish"),
        PinyinUnit("s", "sān", "三", "三 / three"),
        PinyinUnit("y", "yī", "一", "一 / one"),
        PinyinUnit("w", "wǒ", "我", "我 / I"),
    )

    // Common 韵母 (finals)
    val finals: List<PinyinUnit> = listOf(
        PinyinUnit("a", "mā", "妈", "妈 / mother"),
        PinyinUnit("o", "wǒ", "我", "我 / I"),
        PinyinUnit("e", "hē", "喝", "喝 / drink"),
        PinyinUnit("i", "yī", "一", "一 / one"),
        PinyinUnit("u", "wǔ", "五", "五 / five"),
        PinyinUnit("ü", "yú", "鱼", "鱼 / fish"),
        PinyinUnit("ai", "ài", "爱", "爱 / love"),
        PinyinUnit("ei", "bēi", "杯", "杯 / cup"),
        PinyinUnit("ui", "shuǐ", "水", "水 / water"),
        PinyinUnit("ao", "hǎo", "好", "好 / good"),
        PinyinUnit("ou", "gǒu", "狗", "狗 / dog"),
        PinyinUnit("iu", "liù", "六", "六 / six"),
        PinyinUnit("ie", "xiè", "谢", "谢 / thanks"),
        PinyinUnit("üe", "yuè", "月", "月 / moon"),
        PinyinUnit("er", "ér", "儿", "儿 / child"),
        PinyinUnit("an", "kàn", "看", "看 / look"),
        PinyinUnit("en", "mén", "门", "门 / door"),
        PinyinUnit("in", "xīn", "心", "心 / heart"),
        PinyinUnit("un", "lún", "轮", "轮 / wheel"),
        PinyinUnit("ang", "máng", "忙", "忙 / busy"),
        PinyinUnit("eng", "dēng", "灯", "灯 / lamp"),
        PinyinUnit("ing", "míng", "明", "明 / bright"),
        PinyinUnit("ong", "zhōng", "钟", "钟 / clock"),
    )

    // 4 声调 (tones) — shown with the same vowel.
    data class Tone(val mark: String, val name: String, val desc: String)
    val tones: List<Tone> = listOf(
        Tone("mā 妈", "第一声 (阴平)", "高而平 — high & level"),
        Tone("má 麻", "第二声 (阳平)", "由低到高 — rising"),
        Tone("mǎ 马", "第三声 (上声)", "先降后升 — falling-rising"),
        Tone("mà 骂", "第四声 (去声)", "由高到低 — falling"),
    )

    // Single 汉字 → pinyin (used by 音节 & 汉字 practice).
    val characters: List<PinyinPrompt> = listOf(
        PinyinPrompt("我", "wǒ", "wo", "I / me"),
        PinyinPrompt("你", "nǐ", "ni", "you"),
        PinyinPrompt("他", "tā", "ta", "he"),
        PinyinPrompt("好", "hǎo", "hao", "good"),
        PinyinPrompt("是", "shì", "shi", "to be"),
        PinyinPrompt("不", "bù", "bu", "not"),
        PinyinPrompt("人", "rén", "ren", "person"),
        PinyinPrompt("大", "dà", "da", "big"),
        PinyinPrompt("小", "xiǎo", "xiao", "small"),
        PinyinPrompt("中", "zhōng", "zhong", "middle"),
        PinyinPrompt("国", "guó", "guo", "country"),
        PinyinPrompt("家", "jiā", "jia", "home"),
        PinyinPrompt("学", "xué", "xue", "study"),
        PinyinPrompt("爱", "ài", "ai", "love"),
        PinyinPrompt("水", "shuǐ", "shui", "water"),
        PinyinPrompt("火", "huǒ", "huo", "fire"),
        PinyinPrompt("天", "tiān", "tian", "sky"),
        PinyinPrompt("地", "dì", "di", "earth"),
        PinyinPrompt("上", "shàng", "shang", "up"),
        PinyinPrompt("下", "xià", "xia", "down"),
        PinyinPrompt("吃", "chī", "chi", "eat"),
        PinyinPrompt("喝", "hē", "he", "drink"),
        PinyinPrompt("看", "kàn", "kan", "look"),
        PinyinPrompt("听", "tīng", "ting", "listen"),
        PinyinPrompt("说", "shuō", "shuo", "speak"),
        PinyinPrompt("走", "zǒu", "zou", "walk"),
        PinyinPrompt("书", "shū", "shu", "book"),
        PinyinPrompt("字", "zì", "zi", "character"),
        PinyinPrompt("鱼", "yú", "yu", "fish"),
        PinyinPrompt("月", "yuè", "yue", "moon"),
    )

    // Common 词语 (words) → pinyin (no spaces, no tones), used by 词语 practice.
    val words: List<PinyinPrompt> = listOf(
        PinyinPrompt("你好", "nǐ hǎo", "nihao", "hello"),
        PinyinPrompt("谢谢", "xiè xie", "xiexie", "thanks"),
        PinyinPrompt("中国", "zhōng guó", "zhongguo", "China"),
        PinyinPrompt("学习", "xué xí", "xuexi", "to study"),
        PinyinPrompt("老师", "lǎo shī", "laoshi", "teacher"),
        PinyinPrompt("学生", "xué sheng", "xuesheng", "student"),
        PinyinPrompt("朋友", "péng you", "pengyou", "friend"),
        PinyinPrompt("电脑", "diàn nǎo", "diannao", "computer"),
        PinyinPrompt("手机", "shǒu jī", "shouji", "phone"),
        PinyinPrompt("汉语", "hàn yǔ", "hanyu", "Chinese"),
        PinyinPrompt("拼音", "pīn yīn", "pinyin", "pinyin"),
        PinyinPrompt("今天", "jīn tiān", "jintian", "today"),
        PinyinPrompt("明天", "míng tiān", "mingtian", "tomorrow"),
        PinyinPrompt("时间", "shí jiān", "shijian", "time"),
        PinyinPrompt("工作", "gōng zuò", "gongzuo", "work"),
        PinyinPrompt("吃饭", "chī fàn", "chifan", "to eat"),
        PinyinPrompt("喝水", "hē shuǐ", "heshui", "drink water"),
        PinyinPrompt("健康", "jiàn kāng", "jiankang", "health"),
        PinyinPrompt("食品", "shí pǐn", "shipin", "food"),
        PinyinPrompt("北京", "běi jīng", "beijing", "Beijing"),
        PinyinPrompt("上海", "shàng hǎi", "shanghai", "Shanghai"),
        PinyinPrompt("喜欢", "xǐ huan", "xihuan", "to like"),
        PinyinPrompt("漂亮", "piào liang", "piaoliang", "pretty"),
        PinyinPrompt("高兴", "gāo xìng", "gaoxing", "happy"),
        PinyinPrompt("再见", "zài jiàn", "zaijian", "goodbye"),
    )

    // 长句 (sentences) for fast-typing practice. `abbr` is the 简拼 first-letter shortcut —
    // both the full pinyin and the shortcut are accepted, to teach fast input.
    val sentences: List<PinyinPrompt> = listOf(
        PinyinPrompt("你好吗", "nǐ hǎo ma", "nihaoma", "How are you?", "nhm"),
        PinyinPrompt("我爱你", "wǒ ài nǐ", "woaini", "I love you", "wan"),
        PinyinPrompt("谢谢你", "xiè xie nǐ", "xiexieni", "Thank you", "xxn"),
        PinyinPrompt("我是学生", "wǒ shì xué sheng", "woshixuesheng", "I am a student", "wsxs"),
        PinyinPrompt("今天天气好", "jīn tiān tiān qì hǎo", "jintiantianqihao", "The weather is nice today", "jttqh"),
        PinyinPrompt("我喜欢学中文", "wǒ xǐ huan xué zhōng wén", "woxihuanxuezhongwen", "I like learning Chinese", "wxhxzw"),
        PinyinPrompt("中国很大", "zhōng guó hěn dà", "zhongguohenda", "China is very big", "zghd"),
        PinyinPrompt("明天见", "míng tiān jiàn", "mingtianjian", "See you tomorrow", "mtj"),
        PinyinPrompt("请问多少钱", "qǐng wèn duō shao qián", "qingwenduoshaoqian", "How much is it?", "qwdsq"),
        PinyinPrompt("我们一起去", "wǒ men yì qǐ qù", "womenyiqiqu", "Let's go together", "wmyqq"),
    )

    // 长信息 (longer messages) — train sustained fast typing. Type the full pinyin OR the
    // 简拼 first-letter shortcut (`abbr`); both count.
    val paragraphs: List<PinyinPrompt> = listOf(
        PinyinPrompt("你好我叫小明", "nǐ hǎo wǒ jiào xiǎo míng",
            "nihaowojiaoxiaoming", "Hello, my name is Xiao Ming", "nhwjxm"),
        PinyinPrompt("我今天很忙明天见", "wǒ jīn tiān hěn máng míng tiān jiàn",
            "wojintianhenmangmingtianjian", "I'm busy today, see you tomorrow", "wjthmmtj"),
        PinyinPrompt("很高兴认识你", "hěn gāo xìng rèn shi nǐ",
            "hengaoxingrenshini", "Nice to meet you", "hgxrsn"),
        PinyinPrompt("我想喝一杯咖啡", "wǒ xiǎng hē yì bēi kā fēi",
            "woxiangheyibeikafei", "I'd like a cup of coffee", "wxhybkf"),
        PinyinPrompt("周末我们去看电影", "zhōu mò wǒ men qù kàn diàn yǐng",
            "zhoumowomenqukandianying", "Let's see a movie this weekend", "zmwmqkdy"),
        PinyinPrompt("请问洗手间在哪里", "qǐng wèn xǐ shǒu jiān zài nǎ lǐ",
            "qingwenxishoujianzainali", "Where is the restroom?", "qwxsjznl"),
        PinyinPrompt("谢谢你的帮助", "xiè xie nǐ de bāng zhù",
            "xiexienidebangzhu", "Thanks for your help", "xxndbz"),
    )

    // ü → v technique: on a pinyin keyboard, ü is typed as the letter v.
    val vWords: List<PinyinPrompt> = listOf(
        PinyinPrompt("女", "nǚ", "nv", "woman · 用 v 打 ü"),
        PinyinPrompt("绿", "lǜ", "lv", "green · 用 v 打 ü"),
        PinyinPrompt("旅游", "lǚ yóu", "lvyou", "to travel · 用 v 打 ü"),
        PinyinPrompt("女儿", "nǚ ér", "nver", "daughter · 用 v 打 ü"),
        PinyinPrompt("律师", "lǜ shī", "lvshi", "lawyer · 用 v 打 ü"),
        PinyinPrompt("绿色", "lǜ sè", "lvse", "green (color) · 用 v 打 ü"),
        PinyinPrompt("女人", "nǚ rén", "nvren", "woman · 用 v 打 ü"),
        PinyinPrompt("旅行", "lǚ xíng", "lvxing", "trip · 用 v 打 ü"),
    )
}
