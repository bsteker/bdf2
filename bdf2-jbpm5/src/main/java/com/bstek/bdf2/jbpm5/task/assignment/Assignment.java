package com.bstek.bdf2.jbpm5.task.assignment;


/**
 * 自定义任务分配结果对象，其中包含要分配的人的用户名或群组的ID，<br>
 * 如果返回多个人的用户名，那么产生的是一个会签任务，<br>
 * 如果返回不包含人，包含群组ID，那么产生的是竞争任务
 * @author Jacky.gao
 * @since 2013-4-12
 */
public class Assignment {
	private String[] actorIds;
	private String[] groupIds;
	private boolean waitingFor;
	public String[] getActorIds() {
		return actorIds;
	}
	public void setActorIds(String[] actorIds) {
		this.actorIds = actorIds;
	}
	public String[] getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(String[] groupIds) {
		this.groupIds = groupIds;
	}
	public boolean isWaitingFor() {
		return waitingFor;
	}
	public void setWaitingFor(boolean waitingFor) {
		this.waitingFor = waitingFor;
	}
}
