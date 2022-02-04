/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.viewmodellifecycledemo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.skydoves.viewmodel.lifecycle.addViewModelLifecycleObserver
import com.skydoves.viewmodel.lifecycle.viewModelLifecycle
import com.skydoves.viewmodel.lifecycle.viewModelLifecycleOwner
import com.skydoves.viewmodellifecycledemo.databinding.ActivityMainBinding
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber

class SecondActivity : AppCompatActivity() {

  private val viewModel by viewModels<SecondViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    viewModel.viewModelLifecycleOwner.addViewModelLifecycleObserver {
      Timber.d(it.toString())
    }

    binding.button.setOnClickListener {
      viewModel.liveData.value = "on button clicked"
    }

    Timber.d("observer count: " + viewModel.viewModelLifecycleOwner.viewModelLifecycle.getObserverCount())

    val observable = Observable.just(0).subscribe()
    viewModel.compositeDisposable.add(observable)

    Timber.d("disposable count : ${viewModel.compositeDisposable.size()}")

    viewModel.liveData.observe(viewModel.viewModelLifecycleOwner) { }

    Timber.d("has active observers : ${viewModel.liveData.hasActiveObservers()}")
  }

  override fun onDestroy() {
    super.onDestroy()

    Timber.d("current lifecycle state: " + viewModel.viewModelLifecycleOwner.viewModelLifecycle.currentState)

    Timber.d("disposable count : ${viewModel.compositeDisposable.size()}")

    Timber.d("has active observers : ${viewModel.liveData.hasActiveObservers()}")
  }
}
