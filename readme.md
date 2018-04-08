# statetransition

状態遷移をDBで管理するオープンソースライブラリ

## 目的
ドメインごとに状態遷移を作ると時間がかかるので、1箇所で管理できるようにしたい
状態遷移の仕様に即した堅牢な造りにした

## コンセプト
- DBの種類に依存しない
  - 対象: oracle, mysql, sqlite (文字コードはUTF-8)
  - DBの定義は使う側に記述してもらう
- 堅牢に状態遷移する
  - 遷移できない状態への遷移をエラーにする
  - javaのコード側で堅牢にする。テーブルが汎用的なため
- Spring, Mybatisが前提

## 仕様
状態遷移として`申込 -> 契約 -> 解約`を定義した場合を例に記述します
- 状態遷移機能
  - 複数ドメインの状態遷移を1つのテーブルだけで管理できる
  - 状態遷移イベントを挿入できる
  - 最新の状態とその状態になった日時を取得できる
  - 過去の状態についてもデータは保持する
- データ不整合防止機能
  - 定義されていないエントリー状態への遷移はエラー
    - 例えば、何もない状態からいきなり`契約`に遷移するとエラーになる
  - 定義されていない状態遷移はエラー
    - 例えば、`申込 -> 解約`はエラーになる
  - 状態遷移日時を過去にするのはエラー
    - 例えば `申込(2018.01.01) -> 契約(2017.12.31)`はエラーになる
  - 状態遷移イベントの削除は最新のみ
    - 例えば、`申込 -> 契約 -> 解約`と遷移した場合、解除イベントの削除は可能ですが、申込と契約イベントの削除はエラーです。データ不正になるため


## セットアップ

### gradle.build
TODO

### 管理するテーブル名を定義する

resources/application.properties
```
statetransition.table.state_event_table_name = state_event
``` 
state_eventの部分に任意のテーブル名を記述してください  
文字列の前後にダブルクオートはつけないでください！

### テーブルの作成
上記で定義したテーブルを作成してください
#### テーブル定義例:sqlite
```sql
CREATE TABLE state_event (
    state_event_id TEXT NOT NULL UNIQUE,
    id TEXT, 
    state_type TEXT NOT NULL, 
    state TEXT NOT NULL,  
    state_event_date_time DATE NOT NULL,
    event_date_time DATE NOT NULL,
    is_latest NUMBER
);
create index i_state_event_id on state_event(state_event_id);
create index i_id on state_event(id);
create index i_state_type on state_event(state_type);
create index i_state_event_date_time on state_event(state_event_date_time);
create index i_event_date_time on state_event(event_date_time);
create index i_is_latest on state_event(is_latest);
```

### 採番コンポーネントの作成
状態を管理するテーブルで使うIDを生成する採番コンポーネントを作成する    
コンポーネントは`StateEventIdFactory`を継承してください    
**採番はIDを昇順ソート時に生成順になるようにしてください**
例えば
```
EID00001
EID00002
```  
こうゆう感じ

### ドメイン層の作成
SampleStateTypeを参考に  
ドメインと状態と状態遷移を定義してください

#### State(状態)の作成

#### StateTransition(状態遷移)の作成

## 使い方
StateRepositorySpecを参照
