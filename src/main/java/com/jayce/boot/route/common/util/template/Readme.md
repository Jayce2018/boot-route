# 按模板导出Excel模板说明

------------


ExcelOutputWithTemplateUtils工具类使用说明：

------------


对外方法：

**exportLocal：导出到本地磁盘**
- String fileName（导出文件名）
- String excelTemplateUrl（模板路径）
- HashMap<Integer, String[][]> dateMap（嵌入数据集合，key代表sheet，value二位数据集合）
- String exportUrl（导出路径）

------------

**exportResponse：流导出**
- String fileName（导出文件名）
- String excelTemplateUrl（模板路径）
- HashMap<Integer, String[][]> dateMap（嵌入数据集合，key代表sheet，value二位数据集合）
- HttpServletResponse response（http返回对象）
------------


**parseExcelResource：解析模板**
- String uri（模板路径）

------------

### 功能说明:

- 按模板填充数据，模板未涉及的创建单元格按照默认样式指定。
- 模板可跨越sheet填充，以dateMap为准，sheet序号从0开始
- 暂时没有冻结操作参数，模板需手动配置
