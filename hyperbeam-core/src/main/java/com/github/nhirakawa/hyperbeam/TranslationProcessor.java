package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.transform.TranslationModel;

public final class TranslationProcessor {

  private TranslationProcessor() {}

  public static Optional<HitRecord> hit(TranslationModel translationModel, HitRecordParams hitRecordParams) {
    return translationModel.hit(hitRecordParams.getRay(), hitRecordParams.getTMin(), hitRecordParams.getTMax());
  }

}
