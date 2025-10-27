package com.example.project.cartoons.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
class StringFirestoreModel(val stringValue: String?)

@Keep
@Serializable
class BooleanFirestoreModel(val booleanValue: Boolean?)

@Keep
@Serializable
class TimestampFirestoreModel(val timestampValue: String?)

@Keep
@Serializable
class NumberFirestoreModel(val integerValue: Int?)