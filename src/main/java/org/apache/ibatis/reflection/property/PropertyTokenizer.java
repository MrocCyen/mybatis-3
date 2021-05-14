/**
 *    Copyright 2009-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.reflection.property;

import java.util.Iterator;

/**
 * @author Clinton Begin
 */
public class PropertyTokenizer implements Iterator<PropertyTokenizer> {
  private String name;
  private final String indexedName;
  private String index;
  private final String children;

  /**
   * richField
   * richType.richField
   * richMap.key
   * richMap[key]
   * richType.richMap.key
   * richType.richMap[key]
   * richList[0]
   * richType.richList[0]
   *
   */
  public PropertyTokenizer(String fullname) {
    //找到第一个.号的位置
    int delim = fullname.indexOf('.');
    //找到了
    if (delim > -1) {
      //当前名称，比如richType.richField中的richType
      name = fullname.substring(0, delim);
      //下一级名称，比如richType.richField中的richField
      children = fullname.substring(delim + 1);
    } else {
      //没有.
      name = fullname;
      children = null;
    }
    //记录上一次记录的名称
    indexedName = name;
    //找到当前名称中的[符号的位置
    delim = name.indexOf('[');
    //找到了
    if (delim > -1) {
    	//找到[]中的名称，比如richMap[key]中的key
      index = name.substring(delim + 1, name.length() - 1);
      //[之前的名称，比如richMap[key]中的richMap
      name = name.substring(0, delim);
    }
  }

  public String getName() {
    return name;
  }

  public String getIndex() {
    return index;
  }

  public String getIndexedName() {
    return indexedName;
  }

  public String getChildren() {
    return children;
  }

  @Override
  public boolean hasNext() {
    return children != null;
  }

  @Override
  public PropertyTokenizer next() {
    return new PropertyTokenizer(children);
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("Remove is not supported, as it has no meaning in the context of properties.");
  }
}
