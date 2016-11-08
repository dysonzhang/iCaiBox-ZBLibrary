/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.icaihe.adapter;

import java.util.List;

import com.icaihe.model.User;
import com.icaihe.view.UserView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import zuo.biao.library.base.BaseViewAdapter;

/**用户adapter
 * @author Lemon
 */
public class UserAdapter extends BaseViewAdapter<User, UserView> {
	//	private static final String TAG = "UserAdapter";

	public UserAdapter(Activity context, List<User> list) {
		super(context, list);
	}

	@Override
	public UserView createView(int position, View convertView, ViewGroup parent) {
		return new UserView(context, resources);
	}

}