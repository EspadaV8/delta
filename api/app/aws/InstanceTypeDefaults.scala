package io.flow.delta.aws

import io.flow.delta.config.v0.models.InstanceType
import play.api.Logger

object InstanceTypeDefaults {

  def containerMemory(typ: InstanceType): Int = {
    typ match {
      case InstanceType.M4Large => 7500

      case InstanceType.T2Micro => 700
      case InstanceType.T2Small => 1500
      case InstanceType.T2Medium => 3500
      case InstanceType.T2Large => 7500

      case InstanceType.UNDEFINED(other) => {
        Logger.warn(s"Undefined instance type[$other]. Using default memory setting")
        700
      }
    }
  }

}