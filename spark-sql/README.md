RDD(Resilient Distributed Dataset):弹性分布式数据集，抽象类，代表一个弹性，不可变，可分区，其中元素可并行计算的集合

都是弹性分布式数据集
DataFrame：基于RDD，带有schema元信息(列名称，数据类型)，编译器不做类型检查, 相比于RDD的执行性能要高
DataSet：是DataFrame的扩展，有类型安全检查，也有查询优化的特征，使用case class来定义数据的结构信息，是强类型的DataSet[Person]，ds.name