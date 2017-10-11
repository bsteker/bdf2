package com.bstek.bdf2.jbpm4.job.reminder;

import java.util.Date;

/**
 * @author Jacky.gao
 * @since 2013-4-26
 */
public interface ICalculateOverdueTaskReminder {
	/**
	 * 根据业务环境计算当前日期下是否执行任务过期提供动作
	 * @param overdueDays 任务过期天数
	 * @param createDate 任务的创建日期
	 * @return 返回true表示执行任务过期提供动作，false表示不执行
	 */
	boolean calculateOverdue(int overdueDays,Date createDate);
}
