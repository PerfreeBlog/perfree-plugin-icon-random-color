package com.icon.random.color;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.perfree.cache.OptionCacheService;
import com.perfree.plugin.proxy.HtmlRenderProxy;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IconRandomColorHandle extends HtmlRenderProxy {

    @Resource
    private OptionCacheService optionCacheService;

    @Override
    public Document editFrontDocument(Document document, HttpServletResponse response, HttpServletRequest request) {
        if (request.getRequestURI().startsWith("/login") || request.getRequestURI().startsWith("/register")) {
            return document;
        }
        String defaultColors = "#f12711-#f5af19,#fc466b-#3f5efb,#11998e-#38ef7d,#00f260-#0575e6,#03001e-#7303c0-#ec38bc-#fdeff9," +
                "#1a2a6c-#b21f1f-#fdbb2d,#a770ef-#cf8bf3-#fdb99b,#a18cd1-#fbc2eb,#ff8177-#f99185-#cf556c-#b12a5b,#f6d365-#fda085,#d4fc79-#96e6a1,#84fab0-#8fd3f4";
        String colorsStr = optionCacheService.getDefaultValue("ICON_RANDOM_COLORS", "plugin_perfree-plugin-icon-random-color", defaultColors);

        String[] split = colorsStr.split(",");
        List<String> colorArr = new ArrayList<>();
        for (String colors : split) {
            colorArr.add(String.join(", ", colors.split("-")));
        }

        String js = "<script>document.addEventListener(\"DOMContentLoaded\", () => {\n" +
                "  const gradientPool = {};\n" +
                "  const elements = document.querySelectorAll('.fa-solid, .fa-brands, .fa-regular');\n" +
                "  elements.forEach(element => {\n" +
                "    const gradient = gradientPool[Math.floor(Math.random() * gradientPool.length)];\n" +
                "    element.style.backgroundImage = `-webkit-linear-gradient(bottom, ${gradient})`;\n" +
                "    element.style.webkitBackgroundClip = \"text\";\n" +
                "    element.style.webkitTextFillColor = \"transparent\";\n" +
                "  });\n" +
                "});</script>";
        js = StrUtil.format(js, JSONUtil.toJsonStr(colorArr));
        document.body().append(js);
        return document;
    }
}
