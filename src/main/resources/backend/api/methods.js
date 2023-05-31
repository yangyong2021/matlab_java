function getMethodsList (params) {
  return $axios({
    url: '/methods/page',
    method: 'get',
    params
  })
}

// 删除方法接口
function deleteMethod(params) {
  return $axios({
    url: '/methods',
    method: 'delete',
    params
  })
}

// 执行寻优
function executeMethods(params) {
  return $axios({
    url: '/methods/execute',
    method: 'post',
    params
  })

}