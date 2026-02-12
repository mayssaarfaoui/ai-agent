create database aimanager;
use aimanager;

create table if not exists aimanager.graph_node(
    id bigint primary key,
    name varchar(255) not null,
    description varchar(255),
    primary key (id)
);

create table if not exists aimanager.graph_nodes(
    id bigint primary key,
    name varchar(255) not null,
    type varchar(255) not null,
    description varchar(255),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp
);

create table if not exists aimanager.graph_edges(
    id bigint,
    start_node_id bigint not null,
    target_node_id bigint not null,
    status varchar(255) not null,
    replaced_by_edge_id bigint,
    primary key (id),
    foreign key (start_node_id) references aimanager.graph_nodes(id),
    foreign key (target_node_id) references aimanager.graph_nodes(id),
    foreign key (replaced_by_edge_id) references aimanager.graph_edges(id)
);

create table aimanager.commits(
    id bigint not null,
    status varchar(255) not null,
    graph_id bigint not null,
    label TEXT not null,
    created_at timestamp default current_timestamp,
    primary key (id),
    constraint fk_graph_id foreign key (graph_id) references aimanager.graph_node(id)
);

create table if not exists aimanager.commit_graph_nodes(
    commit_id bigint not null,
    graph_node_id bigint not null,
    primary key (commit_id, graph_node_id),
    constraint fk_commit_id foreign key (commit_id) references aimanager.commits(id),
    constraint fk_graph_node_id foreign key (graph_node_id) references aimanager.graph_nodes(id)
);

create table if not exists aimanager.graph_edges(
    id bigint not null,
    start_node_id bigint not null,
    target_node_id bigint not null,
    status varchar(255) not null,
    replaced_by_edge_id bigint,
    primary key (id),
    constraint fk_start_node_id foreign key (start_node_id) references aimanager.graph_nodes(id),
    constraint fk_target_node_id foreign key (target_node_id) references aimanager.graph_nodes(id),
    constraint fk_replaced_by_edge_id foreign key (replaced_by_edge_id) references aimanager.graph_edges(id)
);

create table if not exists aimanager.commit_graph_edges(
    commit_id bigint not null,
    graph_edge_id bigint not null,
    primary key (commit_id, graph_edge_id),
    constraint fk_commit_id foreign key (commit_id) references aimanager.commits(id),
    constraint fk_graph_edge_id foreign key (graph_edge_id) references aimanager.graph_edges(id)
);

create sequence if not exists aimanager.graph_nodes_seq start with 1 increment by 1;
create sequence if not exists aimanager.graph_edges_seq start with 1 increment by 1;
create sequence if not exists aimanager.commits_seq start with 1 increment by 1;