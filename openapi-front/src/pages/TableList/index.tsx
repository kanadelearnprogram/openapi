import { addRule, removeRule, rule, updateRule } from '@/services/ant-design-pro/api';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns, ProDescriptionsItemProps } from '@ant-design/pro-components';
import {
  FooterToolbar,
  ModalForm,
  PageContainer,
  ProDescriptions,
  ProFormText,
  ProFormTextArea,
  ProTable,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Button, Drawer, Input, message } from 'antd';
import React, { useRef, useState } from 'react';

import {
  addInterfaceInfoUsingPost,
  deleteInterfaceInfoUsingPost,
  listInterfaceInfoByPageUsingGet,
  offlineInterfaceInfoUsingPost,
  onlineInterfaceInfoUsingPost,
  updateInterfaceInfoUsingPost,
  //listInterfaceInfoUsingGet
} from "@/services/open-api-front/interfaceInfoController";
import type {SortOrder} from "antd/lib/table/interface";
import {RequestData} from "@ant-design/pro-table/es/typing";
import CreateModel from "@/pages/TableList/components/CreateModel";
import {values} from "lodash";
import UpdateModal from "@/pages/TableList/components/UpdateModal";
import {record} from "@umijs/utils/compiled/zod";

const TableList: React.FC = () => {
  /**
   * @en-US Pop-up window of new window
   * @zh-CN 新建窗口的弹窗
   *  */
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  /**
   * @en-US The pop-up window of the distribution update window
   * @zh-CN 分布更新窗口的弹窗
   * */
  const [updateModalOpen, handleUpdateModalOpen] = useState<boolean>(false);
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.InterfaceInfo>();
  const [selectedRowsState, setSelectedRows] = useState<API.InterfaceInfo[]>([]);

  /**
   * @en-US International configuration
   * @zh-CN 国际化配置
   * */
  const handleAdd = async (fields: API.InterfaceInfo) => {
    const hide = message.loading('正在添加');
    try {
      await addInterfaceInfoUsingPost({
        ...fields,
      });
      hide();
      message.success('Added successfully');
      handleModalVisible(false);//关闭模态框
      actionRef.current?.reload();
      return true;
    } catch (error:any) {
      hide();
      message.error('创建失败',error.message);
      return false;
    }
  };
  const handleUpdate = async (fields: FormValueType) => {
    if (!currentRow) {
      return;
    }
    const hide = message.loading('Configuring');
    try {
      await updateInterfaceInfoUsingPost({
        id: currentRow.id,
        ...fields,
      });
      hide();
      message.success('操作成功');
      return true;
    } catch (error:any) {
      hide();
      message.error('操作失败'+ error.message);
      return false;
    }
  };

  const handleRemove = async (record: API.InterfaceInfo) => {
    const hide = message.loading('正在删除');
    if (!record) return true;
    try {
      await deleteInterfaceInfoUsingPost({
        id: record.id
      });
      hide();
      message.success('删除成功');
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('删除失败，' + error.message);
      return false;
    }
  };
  const handleOffline = async (record:API.IdRequest) =>{
    const hide = message.loading('发布中');
    if (!record) return true;
    try {
      await offlineInterfaceInfoUsingPost({
        id: record.id
      });
      hide();
      message.success('操作成功');
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('删除失败，' + error.message);
      return false;
    }};
  const handleOnline = async (record:API.IdRequest) => {
    const hide = message.loading('发布中');
    if (!record) return true;
    try {
      await onlineInterfaceInfoUsingPost({
        id: record.id
      });
      hide();
      message.success('操作成功');
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('操作失败，' + error.message);
      return false;
    }};

  const columns: ProColumns<API.InterfaceInfo>[] = [
    // todo 添加校验
    /*{
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
    },*/
    {
      title: '接口名称',
      dataIndex: 'name',
      valueType: 'text',
    },
    {
      title: '描述',
      dataIndex: 'description',
      valueType: 'textarea',
    },
    {
      title: '接口地址',
      dataIndex: 'url',
      valueType: 'text',
    },
    {
      title:'请求参数',
      dataIndex: 'requestParams',
      valueType: 'jsonCode',
    },
    {
      title: '请求头',
      dataIndex: 'requestHeader',
      valueType: 'jsonCode',
    },
    {
      title: '响应头',
      dataIndex: 'responseHeader',
      valueType: 'text',
    },
    {
      title: '方法',
      dataIndex: 'method',
      valueType: 'text',
    },
    {
      title: '状态',
      dataIndex: 'status',
      valueEnum: {
        0: {
          text: '关闭',
          status: 'default',
        },
        1: {
          text: '打开',
          status: 'Processing',
        },
      },
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInForm: true,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      valueType: 'dateTime',
      hideInForm: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => {
        const { status } = record;
        return [
          // 修改按钮（始终显示）
          <a
            key="edit"
            onClick={() => {
              handleUpdateModalOpen(true);
              setCurrentRow(record);
            }}
          >
            修改
          </a>,

          // 发布按钮（仅 status=0 时显示）
          status === 0 && (
            <a
              key="publish"
              onClick={() => handleOnline(record)}
            >
              发布
            </a>
          ),

          // 下线按钮（仅 status=1 时显示，带危险样式）
          status === 1 && (
            <Button
              type="text"
              key="offline"
              danger
              onClick={() => handleOffline(record)}
            >
              下线
            </Button>
          ),

          // 删除按钮（始终显示）
          <Button
            type="text"
            key="delete"
            danger
            onClick={() => handleRemove(record)}
          >
            删除
          </Button>
        ].filter(Boolean); // 过滤空元素
      }
    }
  ];
  // @ts-ignore
  return (
    <PageContainer>
      <ProTable<API.RuleListItem, API.PageParams>
        headerTitle={'查询表格'}
        actionRef={actionRef}
        rowKey="key"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalVisible(true);
            }}
          >
            <PlusOutlined /> 新建
          </Button>,
        ]}
        request = { async (params , sort: Record<string, SortOrder>, filter: Record<string, React.ReactText[] | null >) => {
          const res = await listInterfaceInfoByPageUsingGet({
            ...params,
          })
          if (res?.data ) {
            return {
              data: res?.data.records||[],
              success: true,
              total: res.total,
            }

          } else {
            return {
              data: [],
              success: false,
              total: 0,
            }
          }
        }}
        columns={columns}
        rowSelection={{
          onChange: (_, selectedRows) => {
            setSelectedRows(selectedRows);
          },
        }}
      />
      {selectedRowsState?.length > 0 && (
        <FooterToolbar
          extra={
            <div>
              已选择{' '}
              <a
                style={{
                  fontWeight: 600,
                }}
              >
                {selectedRowsState.length}
              </a>{' '}
              项 &nbsp;&nbsp;
              <span>
                服务调用次数总计 {selectedRowsState.reduce((pre, item) => pre + item.callNo!, 0)} 万
              </span>
            </div>
          }
        >
          <Button
            onClick={async () => {
              await handleRemove(selectedRowsState);
              setSelectedRows([]);
              actionRef.current?.reloadAndRest?.();
            }}
          >
            批量删除
          </Button>
          <Button type="primary">批量审批</Button>
        </FooterToolbar>
      )}

      <UpdateModal
        columns={columns}
        onSubmit={async (value) => {
          const success = await handleUpdate(value);
          if (success) {
            handleUpdateModalOpen(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        onCancel={() => {
          handleUpdateModalOpen(false);
          if (!showDetail) {
            setCurrentRow(undefined);
          }
        }}
        visible={updateModalOpen}
        values={currentRow || {}}
      />

      <Drawer
        width={600}
        open={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={false}
      >
        {currentRow?.name && (
          <ProDescriptions<API.RuleListItem>
            column={2}
            title={currentRow?.name}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.name,
            }}
            columns={columns as ProDescriptionsItemProps<API.RuleListItem>[]}
          />
        )}
      </Drawer>
      <CreateModel
        columns={columns}
        // 修改 1：调用 handleModalVisible 而不是 createModalVisible
        onCancel={() => handleModalVisible(false)}
        // 修改 2：确保 onSubmit 返回 Promise
        onSubmit={async (values) => {
          handleAdd(values);
        }}
        visible={createModalVisible}
      />
    </PageContainer>
  );
};
export default TableList;
