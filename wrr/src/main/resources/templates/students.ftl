<style>
    td,th{
        padding: 5px;
        text-align: center;
    }
</style>
<table>
    <thead>
        <tr>
            <th>姓名</th>
            <th>年龄</th>
            <th>性别</th>
            <th>学号</th>
            <th>中文</th>
            <th>英文</th>
            <th>数学</th>
        </tr>
    </thead>
    <tbody>
    <#list students as student>
        <tr>
            <td>${student.name}</td>
            <td>${student.age}</td>
            <td>
                <#if student.sex=='m'>
                    男
                <#elseif student.sex='w'>
                    女
                <#else >
                    未知
                </#if>
            </td>
            <td><#if student.classNo??>${student.classNo}<#else>未知</#if></td>
            <td>${student.course.chinese}</td>
            <td>${student.course.english}</td>
            <td>${student.course.math}</td>
        </tr>
    </#list>
    </tbody>
</table>
<span>共${totalCount}条记录，共${totalPages}页</span>