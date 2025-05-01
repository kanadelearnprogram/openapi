import {
  ProColumns,
  ProTable,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Modal } from 'antd';
import React, {useEffect, useRef} from 'react';
import {ProFormInstance} from "@ant-design/pro-form";
export type Props = {
  values: API.InterfaceInfo;
  columns: ProColumns<API.InterfaceInfo>;
  onCancel: () => void;
  onSubmit: (values: API.InterfaceInfo) => Promise<void>;
  visible: boolean;
};
const UpdateModal: React.FC<Props> = (props) => {
  const { values,visible, columns, onCancel, onSubmit } = props;
  const formRef = useRef<ProFormInstance>();
  // 监听某个变量的变化,改变就触发函数
  useEffect(()=>{
    if (formRef) {
      formRef.current?.setFieldsValue(values);
    }
  },[values]);

  // @ts-ignore
  return (
    <Modal visible={visible} footer={null} onCancel={() => onCancel?.()}>
      <ProTable
        type="form"
        columns={columns}
        formRef={formRef}
        onSubmit={async (value) => {
          onSubmit?.(value);
        }}
      />
    </Modal>
  );
};
export default UpdateModal;
