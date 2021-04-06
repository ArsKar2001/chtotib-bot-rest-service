package com.karmanchik.chtotib_bot_rest_service.model;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;

import java.util.List;

public class ReplacementList {
    private List<Replacement> replacements;

    public List<Replacement> getReplacements() {
        return replacements;
    }

    public void setReplacements(List<Replacement> replacements) {
        this.replacements = replacements;
    }
}
