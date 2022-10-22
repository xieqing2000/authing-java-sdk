package cn.authing.sdk.java.client;

import cn.hutool.core.util.StrUtil;
import cn.authing.sdk.java.dto.*;

import cn.authing.sdk.java.model.AuthingRequestConfig;
import cn.authing.sdk.java.model.ManagementClientOptions;

import java.util.HashMap;
import java.util.Collections;
import java.util.Map;


public class ManagementClient extends BaseClient {

    public ManagementClient(ManagementClientOptions options) {
        super(options);
        // 必要参数校验
        if (StrUtil.isBlank(options.getAccessKeyId())) {
            throw new IllegalArgumentException("accessKeyId is required");
        }
        if (StrUtil.isBlank(options.getAccessKeySecret())) {
            throw new IllegalArgumentException("accessKeySecret is required");
        }
    }

    public Object makeRequest(MakeRequestReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl(reqDto.getUrl());
        config.setBody(reqDto.getData());
        config.setMethod(reqDto.getMethod());
        String response = request(config);
        return deserialize(response, Object.class);
    }

    /**
     * @summary 获取/搜索用户列表
     * @description 此接口用于获取用户列表，支持模糊搜索，以及通过用户基础字段、用户自定义字段、用户所在部门、用户历史登录应用等维度筛选用户。
     * <p>
     * ### 模糊搜素示例
     * <p>
     * 模糊搜索默认会从 `phone`, `email`, `name`, `username`, `nickname` 五个字段对用户进行模糊搜索，你也可以通过设置 `options.fuzzySearchOn`
     * 决定模糊匹配的字段范围：
     * <p>
     * ```json
     * {
     * "query": "北京",
     * "options": {
     * "fuzzySearchOn": [
     * "address"
     * ]
     * }
     * }
     * ```
     * <p>
     * ### 高级搜索示例
     * <p>
     * 你可以通过 `advancedFilter` 进行高级搜索，高级搜索支持通过用户的基础信息、自定义数据、所在部门、用户来源、登录应用、外部身份源信息等维度对用户进行筛选。
     * **且这些筛选条件可以任意组合。**
     * <p>
     * #### 筛选状态为禁用的用户
     * <p>
     * 用户状态（`status`）为字符串类型，可选值为 `Activated` 和 `Suspended`：
     * <p>
     * ```json
     * {
     * "advancedFilter": [
     * {
     * "field": "status",
     * "operator": "EQUAL",
     * "value": "Suspended"
     * }
     * ]
     * }
     * ```
     * <p>
     * #### 筛选邮箱中包含 `@example.com` 的用户
     * <p>
     * 用户邮箱（`email`）为字符串类型，可以进行模糊搜索：
     * <p>
     * ```json
     * {
     * "advancedFilter": [
     * {
     * "field": "email",
     * "operator": "CONTAINS",
     * "value": "@example.com"
     * }
     * ]
     * }
     * ```
     * <p>
     * #### 根据用户的任意扩展字段进行搜索
     * <p>
     * ```json
     * {
     * "advancedFilter": [
     * {
     * "field": "some-custom-key",
     * "operator": "EQUAL",
     * "value": "some-value"
     * }
     * ]
     * }
     * ```
     * <p>
     * #### 根据用户登录次数筛选
     * <p>
     * 筛选登录次数大于 10 的用户：
     * <p>
     * ```json
     * {
     * "advancedFilter": [
     * {
     * "field": "loginsCount",
     * "operator": "GREATER",
     * "value": 10
     * }
     * ]
     * }
     * ```
     * <p>
     * 筛选登录次数在 10 - 100 次的用户：
     * <p>
     * ```json
     * {
     * "advancedFilter": [
     * {
     * "field": "loginsCount",
     * "operator": "BETWEEN",
     * "value": [10, 100]
     * }
     * ]
     * }
     * ```
     * <p>
     * #### 根据用户上次登录时间进行筛选
     * <p>
     * 筛选最近 7 天内登录过的用户：
     * <p>
     * ```json
     * {
     * "advancedFilter": [
     * {
     * "field": "lastLoginTime",
     * "operator": "GREATER",
     * "value": new Date(Date.now() - 7 * 24 * 60 * 60 * 1000)
     * }
     * ]
     * }
     * ```
     * <p>
     * 筛选在某一段时间内登录过的用户：
     * <p>
     * ```json
     * {
     * "advancedFilter": [
     * {
     * "field": "lastLoginTime",
     * "operator": "BETWEEN",
     * "value": [
     * new Date(Date.now() - 14 * 24 * 60 * 60 * 1000),
     * new Date(Date.now() - 7 * 24 * 60 * 60 * 1000)
     * ]
     * }
     * ]
     * }
     * ```
     * <p>
     * #### 根据用户曾经登录过的应用筛选
     * <p>
     * 筛选出曾经登录过应用 `appId1` 或者 `appId2` 的用户：
     * <p>
     * ```json
     * {
     * "advancedFilter": [
     * {
     * "field": "loggedInApps",
     * "operator": "IN",
     * "value": [
     * "appId1",
     * "appId2"
     * ]
     * }
     * ]
     * }
     * ```
     * <p>
     * #### 根据用户所在部门进行筛选
     * <p>
     * ```json
     * {
     * "advancedFilter": [
     * {
     * "field": "department",
     * "operator": "IN",
     * "value": [
     * {
     * "organizationCode": "steamory",
     * "departmentId": "root",
     * "departmentIdType": "department_id",
     * "includeChildrenDepartments": true
     * }
     * ]
     * }
     * ]
     * }
     * ```
     **/
    public UserPaginatedRespDto listUsers(ListUsersRequestDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-users");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, UserPaginatedRespDto.class);
    }

    /**
     * @summary 获取用户列表
     * @description 获取用户列表接口，支持分页，可以选择获取自定义数据、identities 等。
     * @deprecated
     **/
    public UserPaginatedRespDto listUsersLegacy(ListUsersDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-users");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, UserPaginatedRespDto.class);
    }

    /**
     * @summary 获取用户信息
     * @description 通过用户 ID，获取用户详情，可以选择获取自定义数据、identities、选择指定用户 ID 类型等。
     **/
    public UserSingleRespDto getUser(GetUserDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, UserSingleRespDto.class);
    }

    /**
     * @summary 批量获取用户信息
     * @description 通过用户 ID 列表，批量获取用户信息，可以选择获取自定义数据、identities、选择指定用户 ID 类型等。
     **/
    public UserListRespDto getUserBatch(GetUserBatchDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-batch");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, UserListRespDto.class);
    }

    /**
     * @summary 创建用户
     * @description 创建用户，邮箱、手机号、用户名必须包含其中一个，邮箱、手机号、用户名、externalId 用户池内唯一，此接口将以管理员身份创建用户因此不需要进行手机号验证码检验等安全检测。
     **/
    public UserSingleRespDto createUser(CreateUserReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-user");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, UserSingleRespDto.class);
    }

    /**
     * @summary 批量创建用户
     * @description 批量创建用户，邮箱、手机号、用户名必须包含其中一个，邮箱、手机号、用户名、externalId 用户池内唯一，此接口将以管理员身份创建用户因此不需要进行手机号验证码检验等安全检测。
     **/
    public UserListRespDto createUsersBatch(CreateUserBatchReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-users-batch");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, UserListRespDto.class);
    }

    /**
     * @summary 修改用户资料
     * @description 通过用户 ID，修改用户资料，邮箱、手机号、用户名、externalId 用户池内唯一，此接口将以管理员身份修改用户资料因此不需要进行手机号验证码检验等安全检测。
     **/
    public UserSingleRespDto updateUser(UpdateUserReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-user");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, UserSingleRespDto.class);
    }

    /**
     * @summary 批量修改用户资料
     * @description 批量修改用户资料，邮箱、手机号、用户名、externalId 用户池内唯一，此接口将以管理员身份修改用户资料因此不需要进行手机号验证码检验等安全检测。
     **/
    public UserListRespDto updateUserBatch(UpdateUserBatchReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-user-batch");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, UserListRespDto.class);
    }

    /**
     * @summary 删除用户
     * @description 通过用户 ID 列表，删除用户，支持批量删除，可以选择指定用户 ID 类型等。
     **/
    public IsSuccessRespDto deleteUsersBatch(DeleteUsersBatchDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/delete-users-batch");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 获取用户的外部身份源
     * @description 通过用户 ID，获取用户的外部身份源、选择指定用户 ID 类型。
     **/
    public IdentityListRespDto getUserIdentities(GetUserIdentitiesDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-identities");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, IdentityListRespDto.class);
    }

    /**
     * @summary 获取用户角色列表
     * @description 通过用户 ID，获取用户角色列表，可以选择所属权限分组 code、选择指定用户 ID 类型等。
     **/
    public RolePaginatedRespDto getUserRoles(GetUserRolesDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-roles");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, RolePaginatedRespDto.class);
    }

    /**
     * @summary 获取用户实名认证信息
     * @description 通过用户 ID，获取用户实名认证信息，可以选择指定用户 ID 类型。
     **/
    public PrincipalAuthenticationInfoPaginatedRespDto getUserPrincipalAuthenticationInfo(GetUserPrincipalAuthenticationInfoDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-principal-authentication-info");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, PrincipalAuthenticationInfoPaginatedRespDto.class);
    }

    /**
     * @summary 删除用户实名认证信息
     * @description 通过用户 ID，删除用户实名认证信息，可以选择指定用户 ID 类型等。
     **/
    public IsSuccessRespDto resetUserPrincipalAuthenticationInfo(ResetUserPrincipalAuthenticationInfoDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/reset-user-principal-authentication-info");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 获取用户部门列表
     * @description 通过用户 ID，获取用户部门列表，支持分页，可以选择获取自定义数据、选择指定用户 ID 类型、增序或降序等。
     **/
    public UserDepartmentPaginatedRespDto getUserDepartments(GetUserDepartmentsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-departments");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, UserDepartmentPaginatedRespDto.class);
    }

    /**
     * @summary 设置用户所在部门
     * @description 通过用户 ID，设置用户所在部门，可以选择指定用户 ID 类型等。
     **/
    public IsSuccessRespDto setUserDepartments(SetUserDepartmentsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/set-user-departments");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 获取用户分组列表
     * @description 通过用户 ID，获取用户分组列表，可以选择指定用户 ID 类型等。
     **/
    public GroupPaginatedRespDto getUserGroups(GetUserGroupsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-groups");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, GroupPaginatedRespDto.class);
    }

    /**
     * @summary 获取用户 MFA 绑定信息
     * @description 通过用户 ID，获取用户 MFA 绑定信息，可以选择指定用户 ID 类型等。
     **/
    public UserMfaSingleRespDto getUserMfaInfo(GetUserMfaInfoDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-mfa-info");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, UserMfaSingleRespDto.class);
    }

    /**
     * @summary 获取已归档的用户列表
     * @description 获取已归档的用户列表，支持分页，可以筛选开始时间等。
     **/
    public ListArchivedUsersSingleRespDto listArchivedUsers(ListArchivedUsersDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-archived-users");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, ListArchivedUsersSingleRespDto.class);
    }

    /**
     * @summary 强制下线用户
     * @description 通过用户 ID、App ID 列表，强制让用户下线，可以选择指定用户 ID 类型等。
     **/
    public IsSuccessRespDto kickUsers(KickUsersDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/kick-users");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 判断用户是否存在
     * @description 根据条件判断用户是否存在，可以筛选用户名、邮箱、手机号、第三方外部 ID 等。
     **/
    public IsUserExistsRespDto isUserExists(IsUserExistsReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/is-user-exists");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsUserExistsRespDto.class);
    }

    /**
     * @summary 获取用户可访问的应用
     * @description 通过用户 ID，获取用户可访问的应用，可以选择指定用户 ID 类型等。
     **/
    public AppListRespDto getUserAccessibleApps(GetUserAccessibleAppsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-accessible-apps");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, AppListRespDto.class);
    }

    /**
     * @summary 获取用户授权的应用
     * @description 通过用户 ID，获取用户授权的应用，可以选择指定用户 ID 类型等。
     **/
    public AppListRespDto getUserAuthorizedApps(GetUserAuthorizedAppsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-authorized-apps");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, AppListRespDto.class);
    }

    /**
     * @summary 判断用户是否有某个角色
     * @description 通过用户 ID，判断用户是否有某个角色，支持传入多个角色，可以选择指定用户 ID 类型等。
     **/
    public HasAnyRoleRespDto hasAnyRole(HasAnyRoleReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/has-any-role");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, HasAnyRoleRespDto.class);
    }

    /**
     * @summary 获取用户的登录历史记录
     * @description 通过用户 ID，获取用户登录历史记录，支持分页，可以选择指定用户 ID 类型、应用 ID、开始与结束时间戳等。
     **/
    public UserLoginHistoryPaginatedRespDto getUserLoginHistory(GetUserLoginHistoryDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-login-history");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, UserLoginHistoryPaginatedRespDto.class);
    }

    /**
     * @summary 获取用户曾经登录过的应用
     * @description 通过用户 ID，获取用户曾经登录过的应用，可以选择指定用户 ID 类型等。
     **/
    public UserLoggedInAppsListRespDto getUserLoggedinApps(GetUserLoggedinAppsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-loggedin-apps");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, UserLoggedInAppsListRespDto.class);
    }

    /**
     * @summary 获取用户曾经登录过的身份源
     * @description 通过用户 ID，获取用户曾经登录过的身份源，可以选择指定用户 ID 类型等。
     **/
    public UserLoggedInIdentitiesRespDto getUserLoggedinIdentities(GetUserLoggedInIdentitiesDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-logged-in-identities");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, UserLoggedInIdentitiesRespDto.class);
    }

    /**
     * @summary 用户离职
     * @description 通过用户 ID，对用户进行离职操作
     **/
    public ResignUserRespDto resignUser(ResignUserReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/resign-user");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, ResignUserRespDto.class);
    }

    /**
     * @summary 批量用户离职
     * @description 通过用户 ID，对用户进行离职操作
     **/
    public ResignUserRespDto resignUserBatch(ResignUserBatchReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/resign-user-batch");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, ResignUserRespDto.class);
    }

    /**
     * @summary 获取用户被授权的所有资源
     * @description 通过用户 ID，获取用户被授权的所有资源，可以选择指定用户 ID 类型等，用户被授权的资源是用户自身被授予、通过分组继承、通过角色继承、通过组织机构继承的集合。
     **/
    public AuthorizedResourcePaginatedRespDto getUserAuthorizedResources(GetUserAuthorizedResourcesDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-authorized-resources");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, AuthorizedResourcePaginatedRespDto.class);
    }

    /**
     * @summary 检查某个用户在应用下是否具备 Session 登录态
     * @description 检查某个用户在应用下是否具备 Session 登录态
     **/
    public CheckSessionStatusRespDto checkSessionStatus(CheckSessionStatusDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/check-session-status");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, CheckSessionStatusRespDto.class);
    }

    /**
     * @summary 导入用户的 OTP
     * @description 导入用户的 OTP
     **/
    public CommonResponseDto importOtp(ImportOtpReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/import-otp");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, CommonResponseDto.class);
    }

    /**
     * @summary 获取组织机构详情
     * @description 获取组织机构详情
     **/
    public OrganizationSingleRespDto getOrganization(GetOrganizationDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-organization");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, OrganizationSingleRespDto.class);
    }

    /**
     * @summary 批量获取组织机构详情
     * @description 批量获取组织机构详情
     **/
    public OrganizationListRespDto getOrganizationsBatch(GetOrganizationBatchDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-organization-batch");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, OrganizationListRespDto.class);
    }

    /**
     * @summary 获取组织机构列表
     * @description 获取组织机构列表，支持分页。
     **/
    public OrganizationPaginatedRespDto listOrganizations(ListOrganizationsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-organizations");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, OrganizationPaginatedRespDto.class);
    }

    /**
     * @summary 创建组织机构
     * @description 创建组织机构，会创建一个只有一个节点的组织机构，可以选择组织描述信息、根节点自定义 ID、多语言等。
     **/
    public OrganizationSingleRespDto createOrganization(CreateOrganizationReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-organization");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, OrganizationSingleRespDto.class);
    }

    /**
     * @summary 修改组织机构
     * @description 通过组织 code，修改组织机构，可以选择部门描述、新组织 code、组织名称等。
     **/
    public OrganizationSingleRespDto updateOrganization(UpdateOrganizationReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-organization");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, OrganizationSingleRespDto.class);
    }

    /**
     * @summary 删除组织机构
     * @description 通过组织 code，删除组织机构树。
     **/
    public IsSuccessRespDto deleteOrganization(DeleteOrganizationReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/delete-organization");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 搜索组织机构列表
     * @description 通过搜索关键词，搜索组织机构列表，支持分页。
     **/
    public OrganizationPaginatedRespDto searchOrganizations(SearchOrganizationsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/search-organizations");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, OrganizationPaginatedRespDto.class);
    }

    /**
     * @summary 获取部门信息
     * @description 通过组织 code 以及 部门 ID 或 部门 code，获取部门信息，可以获取自定义数据。
     **/
    public DepartmentSingleRespDto getDepartment(GetDepartmentDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-department");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, DepartmentSingleRespDto.class);
    }

    /**
     * @summary 创建部门
     * @description 通过组织 code、部门名称、父部门 ID，创建部门，可以设置多种参数。
     **/
    public DepartmentSingleRespDto createDepartment(CreateDepartmentReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-department");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, DepartmentSingleRespDto.class);
    }

    /**
     * @summary 修改部门
     * @description 通过组织 code、部门 ID，修改部门，可以设置多种参数。
     **/
    public DepartmentSingleRespDto updateDepartment(UpdateDepartmentReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-department");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, DepartmentSingleRespDto.class);
    }

    /**
     * @summary 删除部门
     * @description 通过组织 code、部门 ID，删除部门。
     **/
    public IsSuccessRespDto deleteDepartment(DeleteDepartmentReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/delete-department");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 搜索部门
     * @description 通过组织 code、搜索关键词，搜索部门，可以搜索组织名称等。
     **/
    public DepartmentListRespDto searchDepartments(SearchDepartmentsReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/search-departments");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, DepartmentListRespDto.class);
    }

    /**
     * @summary 获取子部门列表
     * @description 通过组织 code、部门 ID，获取子部门列表，可以选择获取自定义数据、虚拟组织等。
     **/
    public DepartmentPaginatedRespDto listChildrenDepartments(ListChildrenDepartmentsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-children-departments");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, DepartmentPaginatedRespDto.class);
    }

    /**
     * @summary 获取部门成员列表
     * @description 通过组织 code、部门 ID、排序，获取部门成员列表，支持分页，可以选择获取自定义数据、identities 等。
     **/
    public UserPaginatedRespDto listDepartmentMembers(ListDepartmentMembersDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-department-members");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, UserPaginatedRespDto.class);
    }

    /**
     * @summary 获取部门直属成员 ID 列表
     * @description 通过组织 code、部门 ID，获取部门直属成员 ID 列表。
     **/
    public UserIdListRespDto listDepartmentMemberIds(ListDepartmentMemberIdsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-department-member-ids");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, UserIdListRespDto.class);
    }

    /**
     * @summary 搜索部门下的成员
     * @description 通过组织 code、部门 ID、搜索关键词，搜索部门下的成员，支持分页，可以选择获取自定义数据、identities 等。
     **/
    public UserPaginatedRespDto searchDepartmentMembers(SearchDepartmentMembersDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/search-department-members");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, UserPaginatedRespDto.class);
    }

    /**
     * @summary 部门下添加成员
     * @description 通过部门 ID、组织 code，添加部门下成员。
     **/
    public IsSuccessRespDto addDepartmentMembers(AddDepartmentMembersReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/add-department-members");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 部门下删除成员
     * @description 通过部门 ID、组织 code，删除部门下成员。
     **/
    public IsSuccessRespDto removeDepartmentMembers(RemoveDepartmentMembersReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/remove-department-members");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 获取父部门信息
     * @description 通过组织 code、部门 ID，获取父部门信息，可以选择获取自定义数据等。
     **/
    public DepartmentSingleRespDto getParentDepartment(GetParentDepartmentDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-parent-department");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, DepartmentSingleRespDto.class);
    }

    /**
     * @summary 判断用户是否在某个部门下
     * @description 通过组织 code、部门 ID，判断用户是否在某个部门下，可以选择包含子部门。
     **/
    public IsUserInDepartmentRespDto isUserInDepartment(IsUserInDepartmentDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/is-user-in-department");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, IsUserInDepartmentRespDto.class);
    }

    /**
     * @summary 获取分组详情
     * @description 通过分组 code，获取分组详情。
     **/
    public GroupSingleRespDto getGroup(GetGroupDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-group");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, GroupSingleRespDto.class);
    }

    /**
     * @summary 获取分组列表
     * @description 获取分组列表，支持分页。
     **/
    public GroupPaginatedRespDto listGroups(ListGroupsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-groups");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, GroupPaginatedRespDto.class);
    }

    /**
     * @summary 创建分组
     * @description 创建分组，一个分组必须包含分组名称与唯一标志符 code，且必须为一个合法的英文标志符，如 developers。
     **/
    public GroupSingleRespDto createGroup(CreateGroupReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-group");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, GroupSingleRespDto.class);
    }

    /**
     * @summary 批量创建分组
     * @description 批量创建分组，一个分组必须包含分组名称与唯一标志符 code，且必须为一个合法的英文标志符，如 developers。
     **/
    public GroupListRespDto createGroupsBatch(CreateGroupBatchReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-groups-batch");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, GroupListRespDto.class);
    }

    /**
     * @summary 修改分组
     * @description 通过分组 code，修改分组，可以修改此分组的 code。
     **/
    public GroupSingleRespDto updateGroup(UpdateGroupReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-group");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, GroupSingleRespDto.class);
    }

    /**
     * @summary 批量删除分组
     * @description 通过分组 code，批量删除分组。
     **/
    public IsSuccessRespDto deleteGroupsBatch(DeleteGroupsReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/delete-groups-batch");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 添加分组成员
     * @description 添加分组成员，成员以用户 ID 数组形式传递。
     **/
    public IsSuccessRespDto addGroupMembers(AddGroupMembersReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/add-group-members");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 批量移除分组成员
     * @description 批量移除分组成员，成员以用户 ID 数组形式传递。
     **/
    public IsSuccessRespDto removeGroupMembers(RemoveGroupMembersReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/remove-group-members");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 获取分组成员列表
     * @description 通过分组 code，获取分组成员列表，支持分页，可以获取自定义数据、identities、部门 ID 列表。
     **/
    public UserPaginatedRespDto listGroupMembers(ListGroupMembersDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-group-members");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, UserPaginatedRespDto.class);
    }

    /**
     * @summary 获取分组被授权的资源列表
     * @description 通过分组 code，获取分组被授权的资源列表，可以通过资源类型、权限分组 code 筛选。
     **/
    public AuthorizedResourceListRespDto getGroupAuthorizedResources(GetGroupAuthorizedResourcesDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-group-authorized-resources");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, AuthorizedResourceListRespDto.class);
    }

    /**
     * @summary 获取角色详情
     * @description 通过权限分组内角色 code，获取角色详情。
     **/
    public RoleSingleRespDto getRole(GetRoleDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-role");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, RoleSingleRespDto.class);
    }

    /**
     * @summary 分配角色
     * @description 通过权限分组内角色 code，分配角色，被分配者可以是用户或部门。
     **/
    public IsSuccessRespDto assignRole(AssignRoleDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/assign-role");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 移除分配的角色
     * @description 通过权限分组内角色 code，移除分配的角色，被分配者可以是用户或部门。
     **/
    public IsSuccessRespDto revokeRole(RevokeRoleDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/revoke-role");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 获取角色被授权的资源列表
     * @description 通过权限分组内角色 code，获取角色被授权的资源列表。
     **/
    public RoleAuthorizedResourcePaginatedRespDto getRoleAuthorizedResources(GetRoleAuthorizedResourcesDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-role-authorized-resources");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, RoleAuthorizedResourcePaginatedRespDto.class);
    }

    /**
     * @summary 获取角色成员列表
     * @description 通过权限分组内内角色 code，获取角色成员列表，支持分页，可以选择或获取自定义数据、identities 等。
     **/
    public UserPaginatedRespDto listRoleMembers(ListRoleMembersDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-role-members");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, UserPaginatedRespDto.class);
    }

    /**
     * @summary 获取角色的部门列表
     * @description 通过权限分组内角色 code，获取角色的部门列表，支持分页。
     **/
    public RoleDepartmentListPaginatedRespDto listRoleDepartments(ListRoleDepartmentsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-role-departments");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, RoleDepartmentListPaginatedRespDto.class);
    }

    /**
     * @summary 创建角色
     * @description 通过权限分组内角色 code，创建角色，可以选择权限分组、角色描述等。
     **/
    public RoleSingleRespDto createRole(CreateRoleDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-role");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, RoleSingleRespDto.class);
    }

    /**
     * @summary 获取角色列表
     * @description 获取角色列表，支持分页。
     **/
    public RolePaginatedRespDto listRoles(ListRolesDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-roles");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, RolePaginatedRespDto.class);
    }

    /**
     * @summary 删除角色
     * @description 删除角色，可以批量删除。
     **/
    public IsSuccessRespDto deleteRolesBatch(DeleteRoleDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/delete-roles-batch");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 批量创建角色
     * @description 批量创建角色，可以选择权限分组、角色描述等。
     **/
    public IsSuccessRespDto createRolesBatch(CreateRolesBatch reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-roles-batch");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 修改角色
     * @description 通过权限分组内角色新旧 code，修改角色，可以选择角色描述等。
     **/
    public IsSuccessRespDto updateRole(UpdateRoleDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-role");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 获取身份源列表
     * @description 获取身份源列表，可以指定 租户 ID 筛选。
     **/
    public ExtIdpListPaginatedRespDto listExtIdp(ListExtIdpDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-ext-idp");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, ExtIdpListPaginatedRespDto.class);
    }

    /**
     * @summary 获取身份源详情
     * @description 通过 身份源 ID，获取身份源详情，可以指定 租户 ID 筛选。
     **/
    public ExtIdpDetailSingleRespDto getExtIdp(GetExtIdpDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-ext-idp");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, ExtIdpDetailSingleRespDto.class);
    }

    /**
     * @summary 创建身份源
     * @description 创建身份源，可以设置身份源名称、连接类型、租户 ID 等。
     **/
    public ExtIdpSingleRespDto createExtIdp(CreateExtIdpDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-ext-idp");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, ExtIdpSingleRespDto.class);
    }

    /**
     * @summary 更新身份源配置
     * @description 更新身份源配置，可以设置身份源 ID 与 名称。
     **/
    public ExtIdpSingleRespDto updateExtIdp(UpdateExtIdpDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-ext-idp");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, ExtIdpSingleRespDto.class);
    }

    /**
     * @summary 删除身份源
     * @description 通过身份源 ID，删除身份源。
     **/
    public IsSuccessRespDto deleteExtIdp(DeleteExtIdpDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/delete-ext-idp");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 在某个已有身份源下创建新连接
     * @description 在某个已有身份源下创建新连接，可以设置身份源图标、是否只支持登录等。
     **/
    public ExtIdpConnDetailSingleRespDto createExtIdpConn(CreateExtIdpConnDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-ext-idp-conn");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, ExtIdpConnDetailSingleRespDto.class);
    }

    /**
     * @summary 更新身份源连接
     * @description 更新身份源连接，可以设置身份源图标、是否只支持登录等。
     **/
    public ExtIdpConnDetailSingleRespDto updateExtIdpConn(UpdateExtIdpConnDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-ext-idp-conn");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, ExtIdpConnDetailSingleRespDto.class);
    }

    /**
     * @summary 删除身份源连接
     * @description 通过身份源连接 ID，删除身份源连接。
     **/
    public IsSuccessRespDto deleteExtIdpConn(DeleteExtIdpConnDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/delete-ext-idp-conn");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 身份源连接开关
     * @description 身份源连接开关，可以打开或关闭身份源连接。
     **/
    public IsSuccessRespDto changeExtIdpConnState(ChangeExtIdpConnStateDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/change-ext-idp-conn-state");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 租户关联身份源
     * @description 租户可以关联或取消关联身份源连接。
     **/
    public IsSuccessRespDto changeExtIdpConnAssociationState(ChangeExtIdpAssociationStateDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/change-ext-idp-conn-association-state");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 租户控制台获取身份源列表
     * @description 在租户控制台内获取身份源列表，可以根据 应用 ID 筛选。
     **/
    public ExtIdpListPaginatedRespDto listTenantExtIdp(ListTenantExtIdpDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-tenant-ext-idp");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, ExtIdpListPaginatedRespDto.class);
    }

    /**
     * @summary 身份源下应用的连接详情
     * @description 在身份源详情页获取应用的连接情况
     **/
    public ExtIdpListPaginatedRespDto extIdpConnStateByApps(ExtIdpConnAppsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/ext-idp-conn-apps");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, ExtIdpListPaginatedRespDto.class);
    }

    /**
     * @summary 获取用户内置字段列表
     * @description 获取用户内置的字段列表
     **/
    public CustomFieldListRespDto getUserBaseFields() {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-base-fields");
        config.setBody(new Object());
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, CustomFieldListRespDto.class);
    }

    /**
     * @summary 修改用户内置字段配置
     * @description 修改用户内置字段配置，内置字段不允许修改数据类型、唯一性。
     **/
    public CustomFieldListRespDto setUserBaseFields(SetUserBaseFieldsReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/set-user-base-fields");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, CustomFieldListRespDto.class);
    }

    /**
     * @summary 获取自定义字段列表
     * @description 通过主体类型，获取用户、部门或角色的自定义字段列表。
     **/
    public CustomFieldListRespDto getCustomFields(GetCustomFieldsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-custom-fields");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, CustomFieldListRespDto.class);
    }

    /**
     * @summary 创建/修改自定义字段定义
     * @description 创建/修改用户、部门或角色自定义字段定义，如果传入的 key 不存在则创建，存在则更新。
     **/
    public CustomFieldListRespDto setCustomFields(SetCustomFieldsReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/set-custom-fields");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, CustomFieldListRespDto.class);
    }

    /**
     * @summary 设置自定义字段的值
     * @description 给用户、角色或部门设置自定义字段的值，如果存在则更新，不存在则创建。
     **/
    public IsSuccessRespDto setCustomData(SetCustomDataReqDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/set-custom-data");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 获取用户、分组、角色、组织机构的自定义字段值
     * @description 通过筛选条件，获取用户、分组、角色、组织机构的自定义字段值。
     **/
    public GetCustomDataRespDto getCustomData(GetCustomDataDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-custom-data");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, GetCustomDataRespDto.class);
    }

    /**
     * @summary 创建资源
     * @description 创建资源，可以设置资源的描述、定义的操作类型、URL 标识等。
     **/
    public ResourceRespDto createResource(CreateResourceDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-resource");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, ResourceRespDto.class);
    }

    /**
     * @summary 批量创建资源
     * @description 批量创建资源，可以设置资源的描述、定义的操作类型、URL 标识等。
     **/
    public IsSuccessRespDto createResourcesBatch(CreateResourcesBatchDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-resources-batch");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 获取资源详情
     * @description 根据筛选条件，获取资源详情。
     **/
    public ResourceRespDto getResource(GetResourceDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-resource");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, ResourceRespDto.class);
    }

    /**
     * @summary 批量获取资源详情
     * @description 根据筛选条件，批量获取资源详情。
     **/
    public ResourceListRespDto getResourcesBatch(GetResourcesBatchDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-resources-batch");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, ResourceListRespDto.class);
    }

    /**
     * @summary 分页获取资源列表
     * @description 根据筛选条件，分页获取资源详情列表。
     **/
    public ResourcePaginatedRespDto listResources(ListResourcesDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-resources");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, ResourcePaginatedRespDto.class);
    }

    /**
     * @summary 修改资源
     * @description 修改资源，可以设置资源的描述、定义的操作类型、URL 标识等。
     **/
    public ResourceRespDto updateResource(UpdateResourceDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-resource");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, ResourceRespDto.class);
    }

    /**
     * @summary 删除资源
     * @description 通过资源唯一标志符以及所属权限分组，删除资源。
     **/
    public IsSuccessRespDto deleteResource(DeleteResourceDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/delete-resource");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 批量删除资源
     * @description 通过资源唯一标志符以及所属权限分组，批量删除资源
     **/
    public IsSuccessRespDto deleteResourcesBatch(DeleteResourcesBatchDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/delete-resources-batch");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 关联/取消关联应用资源到租户
     * @description 通过资源唯一标识以及权限分组，关联或取消关联资源到租户
     **/
    public IsSuccessRespDto associateTenantResource(AssociateTenantResourceDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/associate-tenant-resource");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 创建权限分组
     * @description 创建权限分组，可以设置分组名称与描述信息。
     **/
    public NamespaceRespDto createNamespace(CreateNamespaceDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-namespace");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, NamespaceRespDto.class);
    }

    /**
     * @summary 批量创建权限分组
     * @description 批量创建权限分组，可以分别设置分组名称与描述信息。
     **/
    public IsSuccessRespDto createNamespacesBatch(CreateNamespacesBatchDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-namespaces-batch");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 获取权限分组详情
     * @description 通过权限分组唯一标志符，获取权限分组详情。
     **/
    public NamespaceRespDto getNamespace(GetNamespaceDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-namespace");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, NamespaceRespDto.class);
    }

    /**
     * @summary 批量获取权限分组详情
     * @description 分别通过权限分组唯一标志符，批量获取权限分组详情。
     **/
    public NamespaceListRespDto getNamespacesBatch(GetNamespacesBatchDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-namespaces-batch");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, NamespaceListRespDto.class);
    }

    /**
     * @summary 修改权限分组信息
     * @description 修改权限分组信息，可以修改名称、描述信息以及新的唯一标志符。
     **/
    public UpdateNamespaceRespDto updateNamespace(UpdateNamespaceDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-namespace");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, UpdateNamespaceRespDto.class);
    }

    /**
     * @summary 删除权限分组信息
     * @description 通过权限分组唯一标志符，删除权限分组信息。
     **/
    public IsSuccessRespDto deleteNamespace(DeleteNamespaceDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/delete-namespace");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 批量删除权限分组
     * @description 分别通过权限分组唯一标志符，批量删除权限分组。
     **/
    public IsSuccessRespDto deleteNamespacesBatch(DeleteNamespacesBatchDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/delete-namespaces-batch");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 授权资源
     * @description 将一个/多个资源授权给用户、角色、分组、组织机构等主体，且可以分别指定不同的操作权限。
     **/
    public IsSuccessRespDto authorizeResources(AuthorizeResourcesDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/authorize-resources");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 获取某个主体被授权的资源列表
     * @description 根据筛选条件，获取某个主体被授权的资源列表。
     **/
    public AuthorizedResourcePaginatedRespDto getAuthorizedResources(GetAuthorizedResourcesDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-authorized-resources");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, AuthorizedResourcePaginatedRespDto.class);
    }

    /**
     * @summary 判断用户是否对某个资源的某个操作有权限
     * @description 判断用户是否对某个资源的某个操作有权限。
     **/
    public IsActionAllowedRespDtp isActionAllowed(IsActionAllowedDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/is-action-allowed");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsActionAllowedRespDtp.class);
    }

    /**
     * @summary 获取资源被授权的主体
     * @description 获取资源被授权的主体
     **/
    public GetResourceAuthorizedTargetRespDto getResourceAuthorizedTargets(GetResourceAuthorizedTargetsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-resource-authorized-targets");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, GetResourceAuthorizedTargetRespDto.class);
    }

    /**
     * @summary 获取同步任务详情
     * @description 获取同步任务详情
     **/
    public SyncTaskSingleRespDto getSyncTask(GetSyncTaskDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-sync-task");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, SyncTaskSingleRespDto.class);
    }

    /**
     * @summary 获取同步任务列表
     * @description 获取同步任务列表
     **/
    public SyncTaskPaginatedRespDto listSyncTasks(ListSyncTasksDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-sync-tasks");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, SyncTaskPaginatedRespDto.class);
    }

    /**
     * @summary 创建同步任务
     * @description 创建同步任务
     **/
    public SyncTaskPaginatedRespDto createSyncTask(CreateSyncTaskDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-sync-task");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, SyncTaskPaginatedRespDto.class);
    }

    /**
     * @summary 修改同步任务
     * @description 修改同步任务
     **/
    public SyncTaskPaginatedRespDto updateSyncTask(UpdateSyncTaskDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-sync-task");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, SyncTaskPaginatedRespDto.class);
    }

    /**
     * @summary 执行同步任务
     * @description 执行同步任务
     **/
    public TriggerSyncTaskRespDto triggerSyncTask(TriggerSyncTaskDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/trigger-sync-task");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, TriggerSyncTaskRespDto.class);
    }

    /**
     * @summary 获取同步作业详情
     * @description 获取同步作业详情
     **/
    public SyncJobSingleRespDto getSyncJob(GetSyncJobDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-sync-job");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, SyncJobSingleRespDto.class);
    }

    /**
     * @summary 获取同步作业详情
     * @description 获取同步作业详情
     **/
    public SyncJobPaginatedRespDto listSyncJobs(ListSyncJobsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-sync-jobs");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, SyncJobPaginatedRespDto.class);
    }

    /**
     * @summary 获取同步作业详情
     * @description 获取同步作业详情
     **/
    public TriggerSyncTaskRespDto listSyncJobLogs(ListSyncJobLogsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-sync-job-logs");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, TriggerSyncTaskRespDto.class);
    }

    /**
     * @summary 获取同步风险操作列表
     * @description 获取同步风险操作列表
     **/
    public SyncRiskOperationPaginatedRespDto listSyncRiskOperations(ListSyncRiskOperationsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-sync-risk-operations");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, SyncRiskOperationPaginatedRespDto.class);
    }

    /**
     * @summary 执行同步风险操作
     * @description 执行同步风险操作
     **/
    public TriggerSyncRiskOperationsRespDto triggerSyncRiskOperations(TriggerSyncRiskOperationDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/trigger-sync-risk-operations");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, TriggerSyncRiskOperationsRespDto.class);
    }

    /**
     * @summary 取消同步风险操作
     * @description 取消同步风险操作
     **/
    public CancelSyncRiskOperationsRespDto cancelSyncRiskOperation(CancelSyncRiskOperationDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/cancel-sync-risk-operation");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, CancelSyncRiskOperationsRespDto.class);
    }

    /**
     * @summary 获取用户行为日志
     * @description 可以选择请求 ID、客户端 IP、用户 ID、应用 ID、开始时间戳、请求是否成功、分页参数去获取用户行为日志
     **/
    public UserActionLogRespDto getUserActionLogs(GetUserActionLogsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-user-action-logs");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, UserActionLogRespDto.class);
    }

    /**
     * @summary 获取管理员操作日志
     * @description 可以选择请求 ID、客户端 IP、操作类型、资源类型、管理员用户 ID、请求是否成功、开始时间戳、结束时间戳、分页来获取管理员操作日志接口
     **/
    public AdminAuditLogRespDto getAdminAuditLogs(GetAdminAuditLogsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-admin-audit-logs");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, AdminAuditLogRespDto.class);
    }

    /**
     * @summary 获取邮件模版列表
     * @description 获取邮件模版列表
     **/
    public GetEmailTemplatesRespDto getEmailTemplates() {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-email-templates");
        config.setBody(new Object());
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, GetEmailTemplatesRespDto.class);
    }

    /**
     * @summary 修改邮件模版
     * @description 修改邮件模版
     **/
    public EmailTemplateSingleItemRespDto updateEmailTemplate(UpdateEmailTemplateDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-email-template");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, EmailTemplateSingleItemRespDto.class);
    }

    /**
     * @summary 预览邮件模版
     * @description 预览邮件模版
     **/
    public PreviewEmailTemplateRespDto previewEmailTemplate(PreviewEmailTemplateDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/preview-email-template");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, PreviewEmailTemplateRespDto.class);
    }

    /**
     * @summary 获取第三方邮件服务配置
     * @description 获取第三方邮件服务配置
     **/
    public EmailProviderRespDto getEmailProvider() {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-email-provider");
        config.setBody(new Object());
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, EmailProviderRespDto.class);
    }

    /**
     * @summary 配置第三方邮件服务
     * @description 配置第三方邮件服务
     **/
    public EmailProviderRespDto configEmailProvider(ConfigEmailProviderDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/config-email-provier");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, EmailProviderRespDto.class);
    }

    /**
     * @summary 获取应用详情
     * @description 通过应用 ID，获取应用详情。
     **/
    public ApplicationSingleRespDto getApplication(GetApplicationDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-application");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, ApplicationSingleRespDto.class);
    }

    /**
     * @summary 获取应用列表
     * @description 获取应用列表
     **/
    public ApplicationPaginatedRespDto listApplications(ListApplicationsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-applications");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, ApplicationPaginatedRespDto.class);
    }

    /**
     * @summary 获取应用简单信息
     * @description 通过应用 ID，获取应用简单信息。
     **/
    public ApplicationSimpleInfoSingleRespDto getApplicationSimpleInfo(GetApplicationSimpleInfoDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-application-simple-info");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, ApplicationSimpleInfoSingleRespDto.class);
    }

    /**
     * @summary 获取应用简单信息列表
     * @description 获取应用简单信息列表
     **/
    public ApplicationSimpleInfoPaginatedRespDto listApplicationSimpleInfo(ListApplicationSimpleInfoDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-application-simple-info");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, ApplicationSimpleInfoPaginatedRespDto.class);
    }

    /**
     * @summary 创建应用
     * @description 创建应用
     **/
    public ApplicationPaginatedRespDto createApplication(CreateApplicationDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-application");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, ApplicationPaginatedRespDto.class);
    }

    /**
     * @summary 删除应用
     * @description 通过应用 ID，删除应用。
     **/
    public IsSuccessRespDto deleteApplication(DeleteApplicationDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/delete-application");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 获取应用密钥
     * @description 获取应用密钥
     **/
    public GetApplicationSecretRespDto getApplicationSecret(GetApplicationSecretDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-application-secret");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, GetApplicationSecretRespDto.class);
    }

    /**
     * @summary 刷新应用密钥
     * @description 刷新应用密钥
     **/
    public RefreshApplicationSecretRespDto refreshApplicationSecret(RefreshApplicationSecretDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/refresh-application-secret");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, RefreshApplicationSecretRespDto.class);
    }

    /**
     * @summary 获取应用当前登录用户
     * @description 获取应用当前处于登录状态的用户
     **/
    public UserPaginatedRespDto listApplicationActiveUsers(ListApplicationActiveUsersDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-application-active-users");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, UserPaginatedRespDto.class);
    }

    /**
     * @summary 获取应用默认访问授权策略
     * @description 获取应用默认访问授权策略
     **/
    public GetApplicationPermissionStrategyRespDto getApplicationPermissionStrategy(GetApplicationPermissionStrategyDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-application-permission-strategy");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, GetApplicationPermissionStrategyRespDto.class);
    }

    /**
     * @summary 更新应用默认访问授权策略
     * @description 更新应用默认访问授权策略
     **/
    public IsSuccessRespDto updateApplicationPermissionStrategy(UpdateApplicationPermissionStrategyDataDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-application-permission-strategy");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 授权应用访问权限
     * @description 给用户、分组、组织或角色授权应用访问权限
     **/
    public IsSuccessRespDto authorizeApplicationAccess(AuthorizeApplicationAccessDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/authorize-application-access");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 删除应用访问授权记录
     * @description 取消给用户、分组、组织或角色的应用访问权限授权
     **/
    public IsSuccessRespDto revokeApplicationAccess(RevokeApplicationAccessDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/revoke-application-access");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, IsSuccessRespDto.class);
    }

    /**
     * @summary 检测域名是否可用
     * @description 检测域名是否可用于创建新应用或更新应用域名
     **/
    public CheckDomainAvailableSecretRespDto checkDomainAvailable(CheckDomainAvailable reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/check-domain-available");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, CheckDomainAvailableSecretRespDto.class);
    }

    /**
     * @summary 获取安全配置
     * @description 无需传参获取安全配置
     **/
    public SecuritySettingsRespDto getSecuritySettings() {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-security-settings");
        config.setBody(new Object());
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, SecuritySettingsRespDto.class);
    }

    /**
     * @summary 修改安全配置
     * @description 可选安全域、Authing Token 有效时间（秒）、验证码长度、验证码尝试次数、用户修改邮箱的安全策略、用户修改手机号的安全策略、Cookie 过期时间设置、是否禁止用户注册、频繁注册检测配置、验证码注册后是否要求用户设置密码、未验证的邮箱登录时是否禁止登录并发送认证邮件、用户自助解锁配置、Authing 登录页面是否开启登录账号选择、APP 扫码登录安全配置进行修改安全配置
     **/
    public SecuritySettingsRespDto updateSecuritySettings(UpdateSecuritySettingsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-security-settings");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, SecuritySettingsRespDto.class);
    }

    /**
     * @summary 获取全局多因素认证配置
     * @description 无需传参获取全局多因素认证配置
     **/
    public MFASettingsRespDto getGlobalMfaSettings() {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-global-mfa-settings");
        config.setBody(new Object());
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, MFASettingsRespDto.class);
    }

    /**
     * @summary 修改全局多因素认证配置
     * @description 传入 MFA 认证因素列表进行修改
     **/
    public MFASettingsRespDto updateGlobalMfaSettings(MFASettingsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-global-mfa-settings");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, MFASettingsRespDto.class);
    }

    /**
     * @summary 获取套餐详情
     * @description 获取当前用户池套餐详情。
     **/
    public CostGetCurrentPackageRespDto getCurrentPackageInfo() {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-current-package-info");
        config.setBody(new Object());
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, CostGetCurrentPackageRespDto.class);
    }

    /**
     * @summary 获取用量详情
     * @description 获取当前用户池用量详情。
     **/
    public CostGetCurrentUsageRespDto getUsageInfo() {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-usage-info");
        config.setBody(new Object());
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, CostGetCurrentUsageRespDto.class);
    }

    /**
     * @summary 获取 MAU 使用记录
     * @description 获取当前用户池 MAU 使用记录
     **/
    public CostGetMauPeriodUsageHistoryRespDto getMauPeriodUsageHistory(GetMauPeriodUsageHistoryDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-mau-period-usage-history");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, CostGetMauPeriodUsageHistoryRespDto.class);
    }

    /**
     * @summary 获取所有权益
     * @description 获取当前用户池所有权益
     **/
    public CostGetAllRightItemRespDto getAllRightsItem() {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-all-rights-items");
        config.setBody(new Object());
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, CostGetAllRightItemRespDto.class);
    }

    /**
     * @summary 获取订单列表
     * @description 获取当前用户池订单列表
     **/
    public CostGetOrdersRespDto getOrders(GetOrdersDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-orders");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, CostGetOrdersRespDto.class);
    }

    /**
     * @summary 获取订单详情
     * @description 获取当前用户池订单详情
     **/
    public CostGetOrderDetailRespDto getOrderDetail(GetOrderDetailDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-order-detail");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, CostGetOrderDetailRespDto.class);
    }

    /**
     * @summary 获取订单支付明细
     * @description 获取当前用户池订单支付明细
     **/
    public CostGetOrderPayDetailRespDto getOrderPayDetail(GetOrderPayDetailDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-order-pay-detail");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, CostGetOrderPayDetailRespDto.class);
    }

    /**
     * @summary 创建 Pipeline 函数
     * @description 创建 Pipeline 函数
     **/
    public PipelineFunctionSingleRespDto createPipelineFunction(CreatePipelineFunctionDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-pipeline-function");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, PipelineFunctionSingleRespDto.class);
    }

    /**
     * @summary 获取 Pipeline 函数详情
     * @description 获取 Pipeline 函数详情
     **/
    public PipelineFunctionSingleRespDto getPipelineFunction(GetPipelineFunctionDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-pipeline-function");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, PipelineFunctionSingleRespDto.class);
    }

    /**
     * @summary 重新上传 Pipeline 函数
     * @description 当 Pipeline 函数上传失败时，重新上传 Pipeline 函数
     **/
    public PipelineFunctionSingleRespDto reuploadPipelineFunction(ReUploadPipelineFunctionDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/reupload-pipeline-function");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, PipelineFunctionSingleRespDto.class);
    }

    /**
     * @summary 修改 Pipeline 函数
     * @description 修改 Pipeline 函数
     **/
    public PipelineFunctionSingleRespDto updatePipelineFunction(UpdatePipelineFunctionDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-pipeline-function");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, PipelineFunctionSingleRespDto.class);
    }

    /**
     * @summary 修改 Pipeline 函数顺序
     * @description 修改 Pipeline 函数顺序
     **/
    public CommonResponseDto updatePipelineOrder(UpdatePipelineOrderDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-pipeline-order");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, CommonResponseDto.class);
    }

    /**
     * @summary 删除 Pipeline 函数
     * @description 删除 Pipeline 函数
     **/
    public CommonResponseDto deletePipelineFunction(DeletePipelineFunctionDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/delete-pipeline-function");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, CommonResponseDto.class);
    }

    /**
     * @summary 获取 Pipeline 函数列表
     * @description 获取 Pipeline 函数列表
     **/
    public PipelineFunctionPaginatedRespDto listPipelineFunctions(ListPipelineFunctionsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-pipeline-functions");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, PipelineFunctionPaginatedRespDto.class);
    }

    /**
     * @summary 获取 Pipeline 日志
     * @description 获取 Pipeline
     **/
    public PipelineFunctionPaginatedRespDto getPipelineLogs(GetPipelineLogsDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-pipeline-logs");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, PipelineFunctionPaginatedRespDto.class);
    }

    /**
     * @summary 创建 Webhook
     * @description 你需要指定 Webhoook 名称、Webhook 回调地址、请求数据格式、用户真实名称来创建 Webhook。还可选是否启用、请求密钥进行创建
     **/
    public CreateWebhookRespDto createWebhook(CreateWebhookDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/create-webhook");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, CreateWebhookRespDto.class);
    }

    /**
     * @summary 获取 Webhook 列表
     * @description 获取 Webhook 列表，可选页数、分页大小来获取
     **/
    public GetWebhooksRespDto listWebhooks(ListWebhooksDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/list-webhooks");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, GetWebhooksRespDto.class);
    }

    /**
     * @summary 修改 Webhook 配置
     * @description 需要指定 webhookId，可选 Webhoook 名称、Webhook 回调地址、请求数据格式、用户真实名称、是否启用、请求密钥参数进行修改 webhook
     **/
    public UpdateWebhooksRespDto updateWebhook(UpdateWebhookDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/update-webhook");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, UpdateWebhooksRespDto.class);
    }

    /**
     * @summary 删除 Webhook
     * @description 通过指定多个 webhookId，以数组的形式进行 webhook 的删除
     **/
    public DeleteWebhookRespDto deleteWebhook(DeleteWebhookDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/delete-webhook");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, DeleteWebhookRespDto.class);
    }

    /**
     * @summary 获取 Webhook 日志
     * @description 通过指定 webhookId，可选 page 和 limit 来获取 webhook 日志
     **/
    public ListWebhookLogsRespDto getWebhookLogs(ListWebhookLogs reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-webhook-logs");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, ListWebhookLogsRespDto.class);
    }

    /**
     * @summary 手动触发 Webhook 执行
     * @description 通过指定 webhookId，可选请求头和请求体进行手动触发 webhook 执行
     **/
    public TriggerWebhookRespDto triggerWebhook(TriggerWebhookDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/trigger-webhook");
        config.setBody(reqDto);
        config.setMethod("POST");
        String response = request(config);
        return deserialize(response, TriggerWebhookRespDto.class);
    }

    /**
     * @summary 获取 Webhook 详情
     * @description 根据指定的 webhookId 获取 webhook 详情
     **/
    public GetWebhookRespDto getWebhook(GetWebhookDto reqDto) {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-webhook");
        config.setBody(reqDto);
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, GetWebhookRespDto.class);
    }

    /**
     * @summary 获取 Webhook 事件列表
     * @description 返回事件列表和分类列表
     **/
    public WebhookEventListRespDto getWebhookEventList() {
        AuthingRequestConfig config = new AuthingRequestConfig();
        config.setUrl("/api/v3/get-webhook-event-list");
        config.setBody(new Object());
        config.setMethod("GET");
        String response = request(config);
        return deserialize(response, WebhookEventListRespDto.class);
    }
}