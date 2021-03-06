/*
 * Copyright (C) 2013 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clover.sdk.v1.app;

import com.clover.sdk.v1.app.AppNotification;
import com.clover.sdk.v1.ResultStatus;

/**
    Provides services related to Clover applications.
 */
interface IAppService {

    /**
        Send a notification to all instances of this app running at the merchant site.
        The notification is queued and sent in the background, so it's safe to make this
        call on the main thread.
    */
    void notify(in AppNotification notification, out ResultStatus resultStatus);
  }
