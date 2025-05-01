export default [
  { path: '/', name: '主页', icon: 'smile', component: './Index' },
  { path: '/interface_count', name: '查看接口', icon: 'smile', component: './InterfaceCount' },
  { path: '/interface_info/:id', name: '查看接口', icon: 'smile', component: './InterfaceInfo',hideInMenu: true },
  {
    path: '/user',
    layout: false,
    routes: [{ name: '登录', path: '/user/login', component: './User/Login' },
              {name:'注册', path: '/user/register', component: './User/Register' },
    ],
  },
  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      {name: '接口管理',icon: 'table', path: '/admin/interface', component: './TableList'},
      {name: '接口分析',path: '/admin/interface_analysis', component: './InterfaceAnalysis' },

    ],
  },
];
