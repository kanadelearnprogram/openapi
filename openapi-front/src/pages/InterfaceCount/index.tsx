import { getInterfaceLeftNumByIdUsingGet } from '@/services/open-api-front/interfaceInfoController';
import {type ActionType, PageContainer, ProColumns, ProTable} from '@ant-design/pro-components';
import { Button, message } from 'antd';
import React, { useRef } from 'react';
import type { API } from '@/services/open-api-front/typings';
import {
  addLeftNumUsingPost,
  listInterfaceInfoByListUsingGet
} from "@/services/open-api-front/userInterfaceInfoController";
import {hide} from "@floating-ui/dom";

const InterfaceNumTable: React.FC = () => {
  const actionRef = useRef<ActionType>();
  const handleAdd = async (record: API.InterfaceNumVO) => {
    try {
      console.log('Adding interface num...', record);
      await addLeftNumUsingPost({ interfaceInfoId: record.id });

      message.success('Added successfully');

      // 手动刷新数据
      const res = await getInterfaceLeftNumByIdUsingGet();
      actionRef.current?.reload();

    } catch (error) {
      message.error('添加次数失败，请检查网络或重试');
    }
  };


  const columns: ProColumns<API.InterfaceNumVO>[] = [
    {
      title: '接口名称',
      dataIndex: 'interfaceName',
    },
    {
      title: '描述',
      dataIndex: 'des',
    },
    {
      title: '剩余次数',
      dataIndex: 'leftNum',
    },
    {
      title: '操作',
      valueType: 'option',
      render: (_, record) => (
        <Button
          type="link"
          danger
          onClick={() => handleAdd(record)}
        >
          添加
        </Button>
      ),
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.InterfaceNumVO>
        headerTitle="接口次数管理"
        actionRef={actionRef}
        rowKey="id"
        request={async () => {
          console.log('Fetching data from API...');
          try {
            const res = await listInterfaceInfoByListUsingGet({

            });
            return {
              data: res.data || [],
              success: true,
              total: res.data?.length || 0,
            };
          } catch (error) {
            message.error('加载失败，请检查登录状态或重试');
            return {
              data: [],
              success: false,
              total: 0,
            };
          }
        }}
        columns={columns}
        pagination={false} // 禁用分页
      />
    </PageContainer>
  );
};

export default InterfaceNumTable;
