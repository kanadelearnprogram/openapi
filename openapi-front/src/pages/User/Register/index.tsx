import { Footer } from '@/components';
import { userRegisterUsingPost } from '@/services/open-api-front/userController';
import {
  LockOutlined,
  MobileOutlined,
} from '@ant-design/icons';
import {
  LoginForm,
  ProFormText,

} from '@ant-design/pro-components';
import { Helmet, history, useModel } from '@umijs/max';
import { Alert, message } from 'antd';
import { createStyles } from 'antd-style';
import React from 'react';
import Settings from '../../../../config/defaultSettings';
import { flushSync } from 'react-dom';

const useStyles = createStyles(({ token }) => ({
  container: {
    display: 'flex',
    flexDirection: 'column',
    height: '100vh',
    overflow: 'auto',
    backgroundImage: "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
    backgroundSize: '100% 100%',
  },
}));

const Login: React.FC = () => {
  const { setInitialState } = useModel('@@initialState');
  const { styles } = useStyles();

  const handleRegister = async (values: API.UserRegisterRequest) => {
    try {
      const res = await userRegisterUsingPost({ ...values });
      if (res.data) {
        message.success(res.message);
        const urlParams = new URL(window.location.href).searchParams;
        history.push(urlParams.get('redirect') || '/');
      }
    } catch (error) {
      message.error('注册失败，请检查输入信息！');
    }
  };

  return (
    <div className={styles.container}>
      <Helmet>
        <title>注册 - {Settings.title}</title>
      </Helmet>
      <div style={{ flex: '1', padding: '32px 0' }}>
        <LoginForm
          contentStyle={{ minWidth: 280, maxWidth: '75vw' }}
          logo={<img alt="logo" src="/logo.svg" />}
          title="注册"
          subTitle="请填写注册信息"
          onFinish={handleRegister}
        >
          <ProFormText
            name="userAccount"
            fieldProps={{
              size: 'large',
              prefix: <MobileOutlined />,
            }}
            placeholder="输入账号"
            rules={[
              { required: true, message: '账号是必填项！' },
              { min: 4, message: '至少4个字符！' },
            ]}
          />
          <ProFormText.Password
            name="userPassword"
            fieldProps={{
              size: 'large',
              prefix: <LockOutlined />,
            }}
            placeholder="请输入密码"
            rules={[
              { required: true, message: '密码是必填项！' },
              { min: 6, message: '密码至少6个字符' },
            ]}
          />
          <ProFormText.Password
            name="checkPassword"
            fieldProps={{
              size: 'large',
              prefix: <LockOutlined />,
            }}
            placeholder="请再次输入密码"
            rules={[
              { required: true, message: '确认密码是必填项！' },
            ]}
          />
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};

export default Login;
