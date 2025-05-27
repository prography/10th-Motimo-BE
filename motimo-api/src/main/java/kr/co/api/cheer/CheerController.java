package kr.co.api.cheer;

import kr.co.api.cheer.docs.CheerControllerSwagger;
import kr.co.api.cheer.rqrs.PhraseReadRs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/cheers")
public class CheerController implements CheerControllerSwagger {

    @GetMapping
    public PhraseReadRs readPhrase() {
        return null;
    }
}
