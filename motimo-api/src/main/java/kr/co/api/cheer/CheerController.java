package kr.co.api.cheer;

import kr.co.api.cheer.docs.CheerControllerSwagger;
import kr.co.api.cheer.rqrs.CheerPhraseRs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/cheers")
public class CheerController implements CheerControllerSwagger {

    @GetMapping
    public CheerPhraseRs readCheerPhrase() {
        return new CheerPhraseRs("오늘도 파이팅이에요!");
    }
}
