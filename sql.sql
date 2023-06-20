create table order_product
(
    order_no       varchar(64) null,
    order_quantity int         null,
    product_no     varchar(64) null
);

create table product_cates
(
    id          int auto_increment comment '主键
'
        primary key,
    pid         int                                   null comment '上级分类',
    ord         int                                   null comment '排序',
    title       varchar(50)                           not null comment '分类名称',
    status      int       default 0                   null comment '状态 0 正常 -1 删除 1 关闭的',
    create_time timestamp default current_timestamp() null,
    modify_time timestamp default current_timestamp() null
);

create table product_list
(
    id               int auto_increment
        primary key,
    product_name     varchar(25)                             not null,
    product_category int         default 1                   null,
    product_price    decimal                                 not null,
    product_desc     text                                    null,
    product_no       varchar(64) default uuid()              not null,
    img              text                                    not null,
    product_hits     mediumtext  default 0                   not null,
    product_status   int         default 0                   not null,
    create_time      timestamp   default current_timestamp() not null,
    modify_time      timestamp   default current_timestamp() not null,
    constraint product_list_product_name_uindex
        unique (product_name),
    constraint product_list_product_no_uindex
        unique (product_no),
    constraint spfl
        foreign key (product_category) references product_cates (id)
);

create table sys_menu
(
    id          bigint auto_increment
        primary key,
    menu_name   varchar(64)  default 'NULL'              not null comment '菜单名',
    path        varchar(200)                             null comment '路由地址',
    component   varchar(255)                             null comment '组件路径',
    visible     char         default '0'                 null comment '菜单状态（0显示 1隐藏）',
    status      char         default '0'                 null comment '菜单状态（0正常 1停用）',
    perms       varchar(100)                             null comment '权限标识',
    icon        varchar(100) default '#'                 null comment '菜单图标',
    create_by   bigint                                   null,
    create_time datetime     default current_timestamp() null,
    update_by   bigint                                   null,
    update_time datetime     default current_timestamp() null,
    del_flag    int          default 0                   null comment '是否删除（0未删除 1已删除）',
    remark      varchar(500)                             null comment '备注'
)
    comment '菜单表' charset = utf8mb4;

create table sys_order
(
    id              int auto_increment comment '订单id'
        primary key,
    order_no        varchar(64)                             not null comment '订单No',
    is_paid         int         default 0                   not null comment '付款标记',
    payment_method  varchar(64) default '默认'              not null comment '支付方式',
    payment_code    varchar(64)                             null comment '支付流水号',
    status          varchar(32) default '0'                 not null,
    create_time     timestamp   default current_timestamp() null,
    modify_time     timestamp   default current_timestamp() null,
    sent_time       timestamp                               null,
    completion_time timestamp                               null,
    gen_time        timestamp                               null
);

create table sys_role
(
    id          bigint auto_increment
        primary key,
    name        varchar(128)                         null,
    role_key    varchar(100)                         null comment '角色权限字符串',
    status      char     default '0'                 null comment '角色状态（0正常 1停用）',
    del_flag    int(1)   default 0                   null comment 'del_flag',
    create_by   bigint(200)                          null,
    create_time datetime default current_timestamp() null,
    update_by   bigint(200)                          null,
    update_time datetime default current_timestamp() null,
    remark      varchar(500)                         null comment '备注'
)
    comment '角色表' charset = utf8mb4;

create table sys_role_menu
(
    role_id bigint(200) auto_increment comment '角色ID',
    menu_id bigint(200) default 0 not null comment '菜单id',
    primary key (role_id, menu_id)
)
    charset = utf8mb4;

create table sys_user_role
(
    user_id varchar(64)           not null comment '用户id',
    role_id bigint(200) default 0 not null comment '角色id',
    primary key (user_id, role_id)
)
    charset = utf8mb4;

create table user_list
(
    id          int auto_increment
        primary key,
    user_name   varchar(16)                              not null,
    user_pass   varchar(256)                             not null,
    user_addr   text                                     null,
    user_phone  varchar(20)                              not null,
    user_auth   varchar(128) default 'normal'            not null,
    user_no     varchar(64)  default uuid()              not null,
    create_time timestamp    default current_timestamp() null,
    modify_time timestamp    default current_timestamp() null,
    constraint user_name
        unique (user_name),
    constraint user_no
        unique (user_no)
);

create table user_order
(
    user_no  varchar(64) not null comment '用户no',
    order_no varchar(64) not null comment '订单no'
);

