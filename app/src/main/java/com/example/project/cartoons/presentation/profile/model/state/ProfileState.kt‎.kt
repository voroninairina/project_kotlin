package com.example.project.cartoons.presentation.profile.model.state

import android.net.Uri

interface ProfileState {
    val name: String
    val photoUri: Uri
}